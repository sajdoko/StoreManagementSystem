package controller;


/**
 * This class acts as an user session.
 * It stores logged in user details.
 * It is constructed with the Singleton Design Pattern.
 *
 * This pattern involves a single class which is responsible to create an object while making sure that only single
 * object gets created. This class provides a way to access its only object which can be accessed directly without
 * need to instantiate the object of the class.
 *
 */
public class UserSessionController {

    private static int userId;
    private static String userFullName;
    private static String userName;
    private static String userEmail;
    private static String userStatus;
    private static int userAdmin;

    /**
     * Create an object of UserSessionController
     */
    private static final UserSessionController instance = new UserSessionController();

    /**
     * Make the constructor private so that this class cannot be instantiated
     */
    private UserSessionController() { }

    /**
     * Get the only object available
     * @return      UserSessionController instance.
     */
    public static UserSessionController getInstance() {
        return instance;
    }


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

    public static void setUserFullName(String userFullName) {
        UserSessionController.userFullName = userFullName;
    }

    public static void setUserName(String userName) {
        UserSessionController.userName = userName;
    }

    public static void setUserEmail(String userEmail) {
        UserSessionController.userEmail = userEmail;
    }

    public static void setUserStatus(String userStatus) {
        UserSessionController.userStatus = userStatus;
    }

    public static void setUserAdmin(int userAdmin) {
        UserSessionController.userAdmin = userAdmin;
    }

    public static void setUserId(int userId) {
        UserSessionController.userId = userId;
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
