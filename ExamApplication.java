import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ExamApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager(scanner);
        QuizManager quizManager = new QuizManager(scanner);

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    if (userManager.login()) {
                        userMenu(userManager, quizManager);
                    }
                    break;
                case 2:
                    userManager.register();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void userMenu(UserManager userManager, QuizManager quizManager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Update Profile");
            System.out.println("2. Change Password");
            System.out.println("3. Take Quiz");
            System.out.println("4. Logout");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    userManager.updateProfile();
                    break;
                case 2:
                    userManager.changePassword();
                    break;
                case 3:
                    quizManager.startQuiz();
                    break;
                case 4:
                    userManager.logout();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}

class UserManager {
    private Scanner scanner;
    private Map<String, User> users;
    private User currentUser;

    public UserManager(Scanner scanner) {
        this.scanner = scanner;
        this.users = new HashMap<>();
    }

    public boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Invalid credentials.");
            return false;
        }
    }

    public void register() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        System.out.print("Enter full name: ");
        String fullName = scanner.next();

        if (users.containsKey(username)) {
            System.out.println("Username already taken.");
        } else {
            users.put(username, new User(username, password, fullName));
            System.out.println("Registration successful.");
        }
    }

    public void updateProfile() {
        if (currentUser == null) {
            System.out.println("No user logged in.");
            return;
        }
        System.out.print("Enter new full name: ");
        String fullName = scanner.next();
        currentUser.setFullName(fullName);
        System.out.println("Profile updated.");
    }

    public void changePassword() {
        if (currentUser == null) {
            System.out.println("No user logged in.");
            return;
        }
        System.out.print("Enter new password: ");
        String newPassword = scanner.next();
        currentUser.setPassword(newPassword);
        System.out.println("Password changed.");
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }
}

class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

class QuizManager {
    private Scanner scanner;
    private Timer timer;
    private boolean isQuizActive;

    public QuizManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startQuiz() {
        if (isQuizActive) {
            System.out.println("A quiz is already in progress.");
            return;
        }

        isQuizActive = true;
        System.out.println("Quiz started. You have 60 seconds to complete it.");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isQuizActive) {
                    System.out.println("\nTime's up! Auto-submitting the quiz.");
                    submitQuiz();
                }
            }
        }, 60000);

        takeQuiz();
    }

    private void takeQuiz() {
        String[] questions = {
            "Question 1: What is the capital of France?\n1. Paris\n2. London\n3. Berlin\n4. Madrid",
            "Question 2: What is 2 + 2?\n1. 3\n2. 4\n3. 5\n4. 6"
        };

        int[] correctAnswers = {1, 2};
        int score = 0;

        for (int i = 0; i < questions.length; i++) {
            System.out.println(questions[i]);
            System.out.print("Your answer: ");
            int answer = scanner.nextInt();
            if (answer == correctAnswers[i]) {
                score++;
            }
        }

        System.out.println("You completed the quiz.");
        submitQuiz(score);
    }

    private void submitQuiz() {
        isQuizActive = false;
        timer.cancel();
        System.out.println("Quiz submitted.");
    }

    private void submitQuiz(int score) {
        isQuizActive = false;
        timer.cancel();
        System.out.println("Quiz submitted. Your score: " + score);
    }
}
