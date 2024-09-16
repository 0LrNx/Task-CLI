package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

public class Task {
    private final int Id;
    private final String description;
    private final String status;
    private final Date createAt;
    private final Date updateAt;

    public Task(int Id, String description, String status, Date createAt, Date updateAt){
        this.Id = Id;
        this.description = description;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("Id", this.Id);
        json.put("description", this.description);
        json.put("status", this.status);
        json.put("createAt", this.createAt.getTime());
        json.put("updateAt", this.updateAt.getTime());
        return json;
    }

    static void addTask(Scanner scanner) {
        System.out.println("Entrez la description de la tâche :");
        scanner.nextLine();
        String description = scanner.nextLine();
        String status = "En cours";

        Task newTask = new Task(generateId(), description, status, new Date(), new Date());

        try {
            String filePath = "tasks.json";
            File file = new File(filePath);

            JSONArray tasksArray;
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                tasksArray = new JSONArray(content);
            } else {
                tasksArray = new JSONArray();
            }

            tasksArray.put(newTask.toJSON());

            Files.write(Paths.get(filePath), tasksArray.toString(2).getBytes());
            System.out.println("Tâche ajoutée avec succès !");
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de l'ajout de la tâche : " + e.getMessage());
        }
    }


    static void modifyTask(Scanner scanner) {
        String filePath = "tasks.json";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Aucune tâche trouvée à modifier.");
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray tasksArray = new JSONArray(content);

            System.out.print("Entrez l'ID de la tâche à modifier : ");
            int taskId = scanner.nextInt();
            scanner.nextLine();

            JSONObject taskToModify = null;
            for (int i = 0; i < tasksArray.length(); i++) {
                JSONObject task = tasksArray.getJSONObject(i);
                if (task.getInt("Id") == taskId) {
                    taskToModify = task;
                    break;
                }
            }

            if (taskToModify == null) {
                System.out.println("Tâche non trouvée avec cet ID.");
                return;
            }

            System.out.println("Entrez la nouvelle description de la tâche (actuelle : " + taskToModify.getString("description") + ") :");
            String newDescription = scanner.nextLine();

            System.out.println("Entrez le nouveau statut de la tâche (actuel : " + taskToModify.getString("status") + ") :");
            String newStatus = scanner.nextLine();

            taskToModify.put("description", newDescription.isEmpty() ? taskToModify.getString("description") : newDescription);
            taskToModify.put("status", newStatus.isEmpty() ? taskToModify.getString("status") : newStatus);
            taskToModify.put("updateAt", new Date().getTime());

            Files.write(Paths.get(filePath), tasksArray.toString(2).getBytes());

            System.out.println("Tâche modifiée avec succès !");
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la modification de la tâche : " + e.getMessage());
        }
    }

    static void deleteTask(Scanner scanner) {
        String filePath = "tasks.json";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Aucune tâche trouvée à supprimer.");
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray tasksArray = new JSONArray(content);

            System.out.print("Entrez l'ID de la tâche à supprimer : ");
            int taskId = scanner.nextInt();
            scanner.nextLine();

            int taskIndex = -1;
            for (int i = 0; i < tasksArray.length(); i++) {
                JSONObject task = tasksArray.getJSONObject(i);
                if (task.getInt("Id") == taskId) {
                    taskIndex = i;
                    break;
                }
            }

            if (taskIndex == -1) {
                System.out.println("Tâche non trouvée avec cet ID.");
                return;
            }

            tasksArray.remove(taskIndex);
            Files.write(Paths.get(filePath), tasksArray.toString(2).getBytes());

            System.out.println("Tâche supprimée avec succès !");
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la suppression de la tâche : " + e.getMessage());
        }
    }

    static void viewTasks(Scanner scanner) {
        String filePath = "tasks.json";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Aucune tâche trouvée.");
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray tasksArray = new JSONArray(content);

            if (tasksArray.isEmpty()) {
                System.out.println("Aucune tâche à afficher.");
                return;
            }

            System.out.println("Que souhaitez-vous visualiser ?");
            System.out.println("[1] - Toutes les tâches.");
            System.out.println("[2] - Tâches en cours.");
            System.out.println("[3] - Tâches terminées (Done).");

            int choice = -1;
            while (choice < 1 || choice > 3) {
                System.out.print("Entrez votre choix (1-3) : ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                } else {
                    System.out.println("Entrée non valide. Veuillez entrer un numéro entre 1 et 3.");
                    scanner.next();
                }
            }

            scanner.nextLine();

            for (int i = 0; i < tasksArray.length(); i++) {
                JSONObject task = tasksArray.getJSONObject(i);
                String status = task.getString("status");

                if (choice == 1 || (choice == 2 && status.equals("En cours")) || (choice == 3 && status.equals("Done"))) {
                    displayTask(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la visualisation des tâches : " + e.getMessage());
        }
    }

    private static void displayTask(JSONObject task) {
        System.out.println("ID : " + task.getInt("Id"));
        System.out.println("Description : " + task.getString("description"));
        System.out.println("Status : " + task.getString("status"));
        System.out.println("Créée le : " + new Date(task.getLong("createAt")));
        System.out.println("Mise à jour le : " + new Date(task.getLong("updateAt")));
        System.out.println("------------------------------------");
    }


    private static int generateId() {
        String filePath = "tasks.json";
        File file = new File(filePath);
        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                JSONArray tasksArray = new JSONArray(content);
                if (!tasksArray.isEmpty()) {
                    JSONObject lastTask = tasksArray.getJSONObject(tasksArray.length() - 1);
                    return lastTask.getInt("Id") + 1;
                }
            } catch (IOException e) {
                System.out.println("Erreur de lecture du fichier : " + e.getMessage());
            }
        }
        return 1;
    }
}
