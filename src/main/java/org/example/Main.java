package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 5) {
            displayBanner();
            displayMenu();
            choice = getChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.println("Ajout de tâche sélectionné.");
                    Task.addTask(scanner);
                    break;
                case 2:
                    System.out.println("Modification de tâche sélectionnée.");
                    Task.modifyTask(scanner);
                    break;
                case 3:
                    System.out.println("Suppression de tâche sélectionnée.");
                    Task.deleteTask(scanner);
                    break;
                case 4:
                    System.out.println("Visualisation des tâches sélectionnée.");
                    Task.viewTasks(scanner);
                    break;
                case 5:
                    System.out.println("Quitter le programme.");
                    break;
                default:
                    System.out.println("Choix non valide.");
            }

            if (choice != 5) {
                System.out.println("\nAppuyez sur Entrée pour revenir au menu...");
                scanner.nextLine();
                scanner.nextLine();
            }
        }

        System.out.println("Merci d'avoir utilisé l'application !");
    }

    private static void displayBanner() {
        System.out.println("""
                                                                        $$\\           $$\\\s
                                                                        \\__|          \\__|
                 $$$$$$\\   $$$$$$\\   $$$$$$\\   $$$$$$$\\       $$$$$$$$\\ $$\\ $$$$$$$$\\ $$\\\s
                $$  __$$\\ $$  __$$\\ $$  __$$\\ $$  _____|      \\____$$  |$$ |\\____$$  |$$ |
                $$ /  $$ |$$ |  \\__|$$ /  $$ |\\$$$$$$\\          $$$$ _/ $$ |  $$$$ _/ $$ |
                $$ |  $$ |$$ |      $$ |  $$ | \\____$$\\        $$  _/   $$ | $$  _/   $$ |
                \\$$$$$$$ |$$ |      \\$$$$$$  |$$$$$$$  |      $$$$$$$$\\ $$ |$$$$$$$$\\ $$ |
                 \\____$$ |\\__|       \\______/ \\_______/       \\________|\\__|\\________|\\__|
                $$\\   $$ |                                                               \s
                \\$$$$$$  |                                                               \s
                 \\______/\s
                """);
    }

    private static void displayMenu() {
        System.out.println("Quelle action voulez-vous faire ?");
        System.out.println("[1] - Ajouter une tâche.");
        System.out.println("[2] - Modifier une tâche.");
        System.out.println("[3] - Supprimer une tâche.");
        System.out.println("[4] - Visualiser vos tâches.");
        System.out.println("[5] - Quitter.");
    }

    private static int getChoice(Scanner scanner) {
        int choice = -1;
        while (choice < 1 || choice > 5) {
            System.out.print("Entrez votre choix (1-5): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Entrée non valide. Veuillez entrer un numéro entre 1 et 5.");
                scanner.next();
            }
        }
        return choice;
    }
}
