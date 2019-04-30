string metauml_defaultFont;
metauml_defaultFont := "pcrr8rn";
input metauml;
beginfig(1);
    Class.JFrame("JFrame")()();

    Class.Main("Driver")
    ()
    (static "- main(String[] args):void",
     "- <<Create>> Driver()");

    Class.Database("Database")
    ("- url:String")
    ("+ <<Create>> Database(String url)",
     "+ connect():void",
     "+ createUserTable():void",
     "+ dropUserTable():void",
     "+ saveUsers(List<User> users):void",
     "+ loadUsers():List<User>",
     "+ createAppTable():void",
     "+ dropAppTable():void",
     "+ saveApps(List<App> apps):void",
     "+ loadApps():List<App>");

    Class.Community("Community")
    ("- users:List<User>")
    ("+ <<Create>> Community(List<User> users)",
     "+ getUsers():List<User>",
     "+ setUsers(List<User> users):void",
     "+ addUser(User user):void",
     "+ sort():void",
     "+ hasUser(String userName):boolean",
     "+ getUser(String userName):User",
     "+ removeUser(String userName):void",
     "+ toString():String");

    Class.Store("Store")
    ("- apps:List<App>")
    ("+ <<Create>> Store(List<App> apps)",
     "+ getApps():List<App>",
     "+ setApps(List<App> apps):void",
     "+ hasApp(int appId):boolean",
     "+ removeUser(int appId):void",
     "+ toString():String");

    Class.User("User")
    ("- userId:int",
     "- userName:String",
     "- userApps:List<Integer>")
    ("+ <<Create>> User(int userId, String userName)",
     "+ getUserId():int",
     "+ setUserId(int userId):void",
     "+ getUserName():String",
     "+ setUserName(String userName):void",
     "+ getUserApps():List<Integer>",
     "+ setUserApps(List<Integer> userApps):void",
     "+ addUserApp(int appId):void",
     "+ isUserApp(int appId):boolean",
     "+ removeUserApp(int appId):void",
     "+ compareTo(Object other):int",
     "+ equals(Object other):boolean",
     "+ toString():String");

    Class.App("App")
    ("- appId:int",
     "- appName:String",
     "- appOwners:List<Integer>",
     "- creatorId:int")
    ("+ <<Create>> App(int appId, String appName, int creatorId)",
     "+ getAppId:int",
     "+ setAppId(int appId):void",
     "+ getAppName():String",
     "+ setAppName(String appName):void",
     "+ getAppCreator():int",
     "+ setAppCreator(int creatorId):void",
     "+ getAppOwners():List<Integer>",
     "+ setAppOwners(List<Integer> appOwners):void",
     "+ addAppOwner(int userId):void",
     "+ isAppOwner(int userId):boolean",
     "+ removeAppOwner(int userId):void",
     "+ compareTo(Object other):int",
     "+ equals(Object other):boolean",
     "+ toString():String");

    Class.UserApp("UserApp")()();

    Note.DatabaseNote("This class implements an SQLite database.");
    Note.JunctionNote("This is a junction, representing the relationship controlled by the SQLite database.");

    Main.n = JFrame.s + (0, -20);
    Database.w = Main.e + (90, 0);
    Community.n = Main.s + (-30, -40);
    Store.n = Main.s + (260, -60);
    User.n = Community.s + (0, -40);
    App.n = Store.s + (0, -40);
    UserApp.n = User.se + (0, -50);
    DatabaseNote.s = Database.n + (0, 15);
    JunctionNote.n = UserApp.s + (0, -20);

    drawObject(JFrame);
    drawObject(Main);
    drawObject(Database);
    drawObject(Community);
    drawObject(User);
    drawObject(Store);
    drawObject(App);
    drawObject(UserApp);
    drawObject(DatabaseNote);
    drawObject(JunctionNote);

    link(inheritance)(Main.n -- JFrame.s);
    link(dependency)(Main.se -- Store.nw);
    link(dependency)(Main.e -- Database.w);
    link(dependency)(Main.s -- Community.n);
    link(aggregation)(Community.s -- User.n);
    link(aggregation)(Store.s -- App.n);
    link(association)(User.s -- UserApp.nw);
    link(association)(UserApp.ne -- App.s);
    clink(dashedLink)(Database.namePict, DatabaseNote);
    clink(dashedLink)(UserApp.namePict, JunctionNote);

    %draw aName.n -- (aName.n + (20, 20));
endfig;
end;
