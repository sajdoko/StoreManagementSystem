package controller;

//singleton design pattern
public final class UserSessionController {

    private static int userId;
    private static UserSessionController instance;
    private static String userFullName;
    private static String userName;
    private static String userEmail;
    private static String userStatus;
    private static int userAdmin;


    public static String getUserFullName() {
        return userFullName;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String getUserStatus() {
        return userStatus;
    }

    public static int getUserAdmin() {
        return userAdmin;
    }

    public UserSessionController(int userId, String userFullName, String userName, String userEmail, int userAdmin, String userStatus) {
        UserSessionController.userId = userId;
        UserSessionController.userFullName = userFullName;
        UserSessionController.userName = userName;
        UserSessionController.userEmail = userEmail;
        UserSessionController.userAdmin = userAdmin;
        UserSessionController.userStatus = userStatus;
    }

    public static UserSessionController getInstance(int userId, String userFullName, String userName, String userEmail, int userAdmin, String userStatus) {
        if (instance == null) {
            instance = new UserSessionController(userId, userFullName, userName, userEmail, userAdmin, userStatus);
        }
        return instance;
    }

    public static void cleanUserSession() {
        userId = 0;
        userFullName = null;
        userName = null;
        userEmail = null;
        userAdmin = 0;
        userStatus = null;
    }

}
