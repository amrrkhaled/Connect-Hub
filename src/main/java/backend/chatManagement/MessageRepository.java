package backend.chatManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageRepository implements IMessageRepository {
    private final String filePath = "data/messages.json";

    @Override
    public List<Message> load() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            if (jsonContent.length() == 0) {
                return new ArrayList<>();
            }

            try {
                JSONArray messagesArray = new JSONArray(jsonContent.toString());
                List<Message> messages = new ArrayList<>();
                for (int i = 0; i < messagesArray.length(); i++) {
                    JSONObject messageJson = messagesArray.getJSONObject(i);
                    String sender = messageJson.getString("sender");
                    String receiver = messageJson.getString("receiver");
                    String content = messageJson.getString("content");
                    messages.add(new Message(sender, receiver, content));
                }
                return messages;
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Failed to parse JSON content from file.");
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to read from file.");
        }
    }

    @Override
    public void save(List<Message> messages) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (Message message : messages) {
            JSONObject messageJson = new JSONObject();
            messageJson.put("sender", message.getSender());
            messageJson.put("receiver", message.getReceiver());
            messageJson.put("content", message.getContent());
            jsonArray.put(messageJson);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the messages as a JSON array string
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to save messages to file.");
        }
    }
}