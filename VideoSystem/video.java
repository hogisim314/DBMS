
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class video {
    public static int Manager() {
        Scanner sc = new Scanner(System.in);
        int cmd = -1;
        System.out.println("0. delete video");
        System.out.println("1. show user's uploaded videos");
        System.out.println("2. show user's watch information");
        System.out.println("3. view manager`s information");
        System.out.println("4. quit");
        cmd = sc.nextInt();
        return cmd;
    }

    public static int User() {
        Scanner sc = new Scanner(System.in);
        int cmd = -1;
        System.out.println("0. watch a video");
        System.out.println("1. upload the video");
        System.out.println("2. manage your uploaded video list");
        System.out.println("3. make a new playlist");
        System.out.println("4. view your list of playlists");
        System.out.println("5. add or delete the video of the playlist");
        System.out.println("6. select playlist and show video list or watch video");
        System.out.println("7. delete the playlist");
        System.out.println("8. quit");
        cmd = sc.nextInt();
        return cmd;
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/videosystem";
            String user = "root";
            String psw = "0000";
            Connection con = DriverManager.getConnection(url, user, psw);
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT numOfUser from manager where managerID="+0);
            int nextuserNum=0;
            while(rs.next()) {
                nextuserNum = rs.getInt("numOfUser");
                break;
            }
            while (true) {
                System.out.println("---------------------");
                System.out.println("Who are you?");
                System.out.println("0.Exit");
                System.out.println("1.Manager");
                System.out.println("2.User");
                int cmd = -1;
                Scanner s = new Scanner(System.in);
                cmd = s.nextInt();
                if (cmd == 0) {
                    con.close();
                    stmt.close();
                    return; //program returns
                } else if (cmd == 1) { //manager
                    int tmp = Manager();
                    switch (tmp) {
                        case 0: //delete video
                            s.nextLine();
                            System.out.println("input the title of the video");
                            String title = s.nextLine();
                            title = "'" + title + "'";
                           rs = stmt.executeQuery("Select videoID from video where videoTitle=" + title);
                            if (!rs.next()) {
                                System.out.println("no such video!");
                                continue;
                            }
                            rs = stmt.executeQuery("Select videoID,videoTitle,ownerID from video where videoTitle=" + title);
                            int index = 1;
                            System.out.println("=====================");
                            while (rs.next()) {
                                title = rs.getString("videoTitle");
                                int owner_id = rs.getInt("ownerID");
                                int video_id = rs.getInt("videoID");
                                System.out.println(index + "." + title + "-" + owner_id + " #videoID: " + video_id);
                                index++;
                            }
                            System.out.println("=====================");
                            System.out.println("input the id of the video that you want to delete");
                            int videoid = s.nextInt();
                            String videoidstr = Integer.toString(videoid);
                            videoidstr="'"+videoidstr+"'";
                            stmt.executeUpdate("Delete from construct where c_videoID=" + videoidstr);
                            stmt.executeUpdate("Delete from watch where w_videoID=" + videoidstr);
                            stmt.executeUpdate("Delete from video where videoID=" + videoidstr);
                            stmt.executeUpdate("UPDATE manager set numOfVideo=numOfVideo-1 WHERE managerID="+0);
                            System.out.println("videoID : " + videoid + " deleted because of company policy");
                            break;
                        case 1: // show user's uploaded videos.
                            s.nextLine();
                            rs = stmt.executeQuery("SELECT * FROM USER ");
                            while(rs.next()) {
                                int userID = rs.getInt("userID");
                                String userName = rs.getString("name");
                                System.out.println("userID : " + userID + " name : " + userName);
                            }
                            System.out.println("input the user's id");
                            int userid = s.nextInt();
                            rs = stmt.executeQuery("Select * From video Where ownerID=" + userid);
                            if (!rs.next()) {
                                ResultSet rs1 = stmt.executeQuery("SELECT * FROM USER WHERE USERID="+userid);
                                if (rs1.next()) {
                                    System.out.println("userID : "+userid+" didn't upload any videos!");
                                    continue;
                                }
                                System.out.println("no such user!");
                                System.out.println("input the user's id again");
                                userid = s.nextInt();
                            }
                            rs = stmt.executeQuery("Select * From video Where ownerID=" + userid);
                            while (rs.next()) {
                                String videoTitle = rs.getString("videoTitle");
                                int videoID = rs.getInt("videoID");
                                Date upload_date = rs.getDate("upload_date");
                                int ownerID = rs.getInt("ownerID");
                                System.out.println("#userID: " + ownerID + " uploaded video ID, videoTitle: " + videoID + ", " + videoTitle + " on " + upload_date);
                            }
                            break;
                        case 2://show user's watch information
                            s.nextLine();
                            rs = stmt.executeQuery("SELECT * FROM USER ");
                            while(rs.next()) {
                                int userID = rs.getInt("userID");
                                String userName = rs.getString("name");
                                System.out.println("userID : " + userID + " name : " + userName);
                            }
                            System.out.println("input the user's id");
                            userid = s.nextInt();
                            rs = stmt.executeQuery("SELECT * from video");
                            while(rs.next()) {
                                videoid = rs.getInt("videoID");
                                String videoTitle = rs.getString("videoTitle");
                                System.out.println("#videoid : "+videoid+" videoTitle : "+videoTitle);
                            }
                            System.out.println("input the videoID");
                            videoid = s.nextInt();
                            videoidstr=Integer.toString(videoid);
                            String useridstr = Integer.toString(userid);
                            videoidstr="'"+videoidstr+"'";
                            useridstr = "'"+useridstr+"'";
                            rs = stmt.executeQuery("Select * From watch Where w_userID=" + useridstr+"and w_videoID="+videoidstr);
                            if (!rs.next()) {//기록이 없다면 안봤을 수도 아니면 아이디 잘못친거일수도
                                ResultSet rs1 = stmt.executeQuery("SELECT * FROM user WHERE userID=" + useridstr);
                                if (!rs1.next()) {//아이디 잘못친거
                                    System.out.println("no such user!");

                                } else {//기록이 없는거
                                    System.out.println("userID: " + userid + " did not watch that videos.");
                                }
                            }
                            else {
                                rs = stmt.executeQuery("Select * From watch Where w_userID=" + useridstr+"and w_videoID="+videoidstr);
                                while (rs.next()) {
                                    int w_userid = rs.getInt("w_userID");
                                    int w_videoid = rs.getInt("w_videoID");
                                    int watch_num = rs.getInt("watch_num");
                                    System.out.println("#userID: " + w_userid + " watched videoID: " + w_videoid + " " + watch_num + " times");
                                }
                                continue;
                            }
                            System.out.println("input the user's id");
                            userid = s.nextInt();
                            rs = stmt.executeQuery("SELECT * from video");
                            while(rs.next()) {
                                videoid = rs.getInt("videoID");
                                String videoTitle = rs.getString("videoTitle");
                                System.out.println("#videoid : "+videoid+" videoTitle : "+videoTitle);
                            }
                            System.out.println("input the videoID");
                            videoid = s.nextInt();
                            videoidstr=Integer.toString(videoid);
                            useridstr = Integer.toString(userid);
                            videoidstr="'"+videoidstr+"'";
                            useridstr = "'"+useridstr+"'";
                            rs = stmt.executeQuery("Select * From watch Where w_userID=" + useridstr+"and w_videoID="+videoidstr);
                            if (!rs.next()) {//기록이 없다면 안봤을 수도 아니면 아이디 잘못친거일수도
                                ResultSet rs1 = stmt.executeQuery("SELECT * FROM user WHERE userID=" + useridstr);
                                if (!rs1.next()) {//아이디 잘못친거
                                    System.out.println("no such user!");

                                } else {//기록이 없는거
                                    System.out.println("userID: " + userid + " did not watch that videos.");
                                }
                            }
                            else {
                                rs = stmt.executeQuery("Select * From watch Where w_userID=" + useridstr + "and w_videoID=" + videoidstr);
                                while (rs.next()) {
                                    int w_userid = rs.getInt("w_userID");
                                    int w_videoid = rs.getInt("w_videoID"); //
                                    int watch_num = rs.getInt("watch_num");
                                    System.out.println("#userID: " + w_userid + " watched videoID: " + w_videoid + " " + watch_num + " times");
                                }
                            }
                            break;
                        case 3: //view manager's information
                            s.nextLine();
                            System.out.println("input the manager's id");
                            int mgrid = s.nextInt();
                            rs = stmt.executeQuery("Select * From manager Where managerID=" + mgrid);
                            if (!rs.next()) {
                                System.out.println("no such manager!");
                                System.out.println("input the user's id again");
                                userid = s.nextInt();
                            }
                            rs = stmt.executeQuery("Select * From manager Where managerID=" + mgrid);
                            while (rs.next()) {
                                int managerID = rs.getInt("managerID");
                                int numOfUser = rs.getInt("numOfUser");
                                String name = rs.getString("name");
                                int numOfVideo = rs.getInt("numOfVideo");
                                System.out.println("#managerID: " + managerID + " name: " + name + " numOfUser : " + numOfUser + " numOfVideo : " + numOfVideo);
                            }
                            break;
                        case 4:
                            continue;
                    }
                } else {//user
                    System.out.println("input your id please(ex>'11111111')");
                    String inputID = s.next();
                    String inputIDstr = inputID;
                    inputIDstr ="'"+inputIDstr+"'";
                    rs = stmt.executeQuery("Select Id,password from user where ID="+inputIDstr);
                    if(!rs.next()) {//no such ID
                        System.out.println("no such ID, Do you want to register? input y for yes, n for no");
                        char response = s.next().charAt(0);
                        if (response == 'y') {//register process
                            System.out.println("Input your information!");
                            System.out.print("ID: ");
                            inputIDstr = s.next();
                            inputIDstr = "'" + inputIDstr + "'";
                            System.out.print("password: ");
                            int password = s.nextInt();
                            System.out.print("name: ");
                            String name = s.next();
                            name = "'" + name + "'";
                            System.out.print("alias: ");
                            String alias = s.next();
                            alias = "'" + alias + "'";
                            System.out.print("phoneNum: ");
                            int phoneNum = s.nextInt();
                            Statement st1 = con.createStatement();
                            nextuserNum++;
                            st1.executeUpdate("insert into user(userID,ID,password,name,alias,phoneNum,mngerID) values("
                                    + nextuserNum + "," + inputIDstr + "," + password + "," + name + "," + alias + "," + phoneNum + "," + 0 + ")");
                            stmt.executeUpdate("UPDATE manager set numOfUser=numOfUser+1");
                            System.out.println("input your id please(ex>'11111111')");
                            inputIDstr = s.next();
                            inputIDstr ="'"+inputIDstr+"'";
                        }
                    }
                    rs = stmt.executeQuery("Select ID,userID,password from user where ID="+inputIDstr);
                    int userID = 0;
                    int password = -1;
                    String ID="";
                    if(rs.next()) {
                        ID = rs.getString("ID");
                        userID = rs.getInt("userID");
                        password = rs.getInt("password");
                    }
                    String userIDstr = Integer.toString(userID);
                    userIDstr = "'"+userIDstr+"'";
                    System.out.println("input your password please(ex>'1535')");
                    int inputpassword = s.nextInt();
                    if (inputpassword != password) {
                        System.out.println("wrong password!");
                        continue;
                    }
                    int res = User();
                    switch(res) {
                        case 0: //watch a video
                            System.out.println("input the videoID you want to watch");
                            int videoID = s.nextInt();
                            rs = stmt.executeQuery("SELECT videoTitle, videoID, ownerID from video WHERE videoID=" + videoID);
                            if (!rs.next()) {//video not exists
                                System.out.println("There is no videoID : " + videoID);
                            } else {
                                String videoTitle = rs.getString("videoTitle");
                                videoID = rs.getInt("videoID");
                                int ownerID = rs.getInt("ownerID");
                                System.out.println("ID: " + ID + " watched videoID: " + videoID + " videoTitle: " + videoTitle + " uploaded by ownerID: " + ownerID);
                                String videoIDstr = Integer.toString(videoID);
                                videoIDstr = "'" + videoIDstr + "'";
                                stmt.executeUpdate("update video set views=views+1 where videoID="+videoIDstr);
                                rs = stmt.executeQuery("SELECT * FROM watch WHERE w_videoID=" + videoIDstr + "and w_userID=" + userIDstr);
                                if (!rs.next()) {
                                    stmt.executeUpdate("INSERT into watch(w_userID,w_videoID,watch_num) values("+userIDstr+","+videoIDstr+",'1')");
                                } else {
                                    stmt.executeUpdate("UPDATE watch set watch_num=watch_num+1 WHERE w_videoID=" + videoIDstr + "and w_userID=" + userIDstr);
                                }
                            }
                            break;
                        case 1://upload the video.
                            System.out.println("Write down your video Information");
                            System.out.print("Title : ");
                            String title = s.next();
                            title = "'"+title+"'";
                            System.out.print("category : ");
                            String category = s.next();
                            category = "'"+category+"'";
                            rs = stmt.executeQuery("SELECT Max(videoID) from video");
                            videoID=0;
                            if (rs.next()) {
                                videoID=rs.getInt(1)+1;//
                            }
                            String videoIDstr = Integer.toString(videoID);
                            videoIDstr="'"+videoIDstr+"'";
                            System.out.print("playtime : ");
                            int playtime = s.nextInt();
                            String playtimestr = Integer.toString(playtime);
                            playtimestr = "'"+playtimestr+"'";
                            System.out.print("upload_date ex) 2022-12-05 : ");
                            String input_upload_date = s.next();
                            input_upload_date="'"+input_upload_date+"'";
                            stmt.executeUpdate("Insert into video(videoTitle, category, videoID, playtime, views, upload_date, ownerID, videomgrID) values(" +
                                    ""+title+","+category+","+videoIDstr+","+playtime+","+0+","+input_upload_date+","+userIDstr+","+0+")");
                            System.out.println("Insert completed!");
                            rs = stmt.executeQuery("SELECT * from playlist where listID="+userIDstr);
                            if (!rs.next()) {//없으면 만들고 넣기
                                String listname = inputID+"playlist";
                                listname = "'"+listname+"'";
                                stmt.executeUpdate("insert into playlist(listID, listName, numOfVideo, CreaterID, mgrID) values("
                                        +userIDstr+","+listname+","+'1'+","+userIDstr+","+'0'+")");
                                stmt.executeUpdate("insert into construct(c_listID, c_videoID) values("+userIDstr+","+videoIDstr+")");
                            }else {//있으면 그냥 넣기
                                stmt.executeUpdate("insert into construct(c_listID, c_videoID) values("+userIDstr+","+videoIDstr+")");
                            }
                            stmt.executeUpdate("update manager set numOfVideo=numOfVideo+1 where managerID="+0);
                            break;
                        case 2://manage your uploaded video list
                            rs = stmt.executeQuery("SELECT * from playlist where listID="+userIDstr);
                            if (!rs.next()) {//본인 영상 업로드 플리 없음
                                System.out.println("You haven't uploaded any video yet");
                                continue;
                            }
                            //업로드했던 영상들 출력
                            rs = stmt.executeQuery("SELECT videoTitle, videoID, upload_date from video as v where v.videoID in " +
                                    "(SELECT c_videoID from construct as c where c_listID="+userIDstr+")");//여기 다시 보기 괄호 이상함
                            while(rs.next()) {
                                //내가 업로드했던 영상들 출력.
                                String videoTitle = rs.getString("videoTitle");
                                videoID = rs.getInt("videoID");
                                Date upload_date = rs.getDate("upload_date");
                                System.out.println("#videoTitle : "+videoTitle+" videoID : "+videoID+" upload_date : "+upload_date);
                            }
                            //있음->delete하거나 공유하거나
                            System.out.println("Input 0 for delete, 1 for share your playlist");
                            cmd = s.nextInt();
                            if (cmd==0) {
                                System.out.println("Select videoID you want to delete");
                                videoIDstr=s.next();
                                videoIDstr="'"+videoIDstr+"'";
                                stmt.executeUpdate("Delete from construct where c_videoID=" + videoIDstr);
                                stmt.executeUpdate("Delete from watch where w_videoID=" + videoIDstr);
                                stmt.executeUpdate("Delete from video where videoID=" + videoIDstr);
                                stmt.executeUpdate("UPDATE manager set numOfVideo=numOfVideo-1");
                                System.out.println("videoID : " + videoIDstr + " has been deleted.");
                            }else {//share
                                System.out.println("your playlist has been shared to kakaotalk.");
                            }
                            break;
                        case 3://make your own playlist
                            System.out.println("Create the title of your playlist");
                            String listname = s.next();
                            listname = "'" + listname + "'";
                            rs = stmt.executeQuery("Select Max(listID) from playlist");
                            int newlistID = 0;
                            if (rs.next()) {
                                newlistID = rs.getInt(1) + 1;
                            }
                            String newlistIDstr = Integer.toString(newlistID);
                            newlistIDstr="'"+newlistIDstr+"'";
                            stmt.executeUpdate("Insert into playlist(listID,listName,numOfVideo,CreaterID,mgrID) values(" + newlistIDstr + "," +
                                    listname + "," + '0' + "," + userIDstr + "," + '0' + ")");
                            System.out.println("New playlist is created. You can add videos in : " + listname);
                            break;
                        case 4://view your list of playlists
                            rs=stmt.executeQuery("Select listName from playlist where Createrid="+userIDstr);
                            if(rs.next()) {
                                System.out.println("<Your playlists>");
                                listname=rs.getString(1);
                                System.out.println(listname);
                                while(rs.next()) {
                                    listname = rs.getString(1);
                                    System.out.println(listname);
                                }
                            }else {
                                System.out.println("You don't have any playlists!");
                            }
                            break;
                        case 5://add or delete the video of the playlist
                            rs=stmt.executeQuery("Select listID,listName from playlist where Createrid="+userIDstr);
                            int listID=0;
                            String listIDstr="";
                            if(rs.next()) {
                                System.out.println("<Your playlists>");
                                listID=rs.getInt("listID");
                                listIDstr=Integer.toString(listID);
                                listIDstr="'"+listIDstr+"'";
                                listname = rs.getString("listName");
                                listname="'"+listname+"'";
                                System.out.println("listID : "+listID+", listName : "+listname);
                                while(rs.next()) {
                                    listID=rs.getInt("listID");
                                    listname = rs.getString("listName");
                                    System.out.println("listID : "+listID+", listName : "+listname);
                                }
                            }else {
                                System.out.println("You don't have any playlists");
                                continue;
                            }
                            System.out.println("input the listID of the playlist you want to handle");
                            int num = s.nextInt();
                            listIDstr = Integer.toString(num);
                            listIDstr="'"+listIDstr+"'";
                            System.out.println("0 for add 1 for delete video");
                            int cmdNum = s.nextInt();

                            if(cmdNum==0) { //add
                                s.nextLine();
                                System.out.println("input the title of the video that you want to add");
                                String videoTitle = s.nextLine();
                                videoTitle="'"+videoTitle+"'";
                                rs=stmt.executeQuery("Select videoTitle,videoID from video where videoTitle="+videoTitle);
                                if(rs.next()) {
                                    videoTitle = rs.getString("videoTitle");
                                    videoID= rs.getInt("videoID");
                                    System.out.println("videoID : "+videoID+", videoTitle : "+videoTitle);
                                    while(rs.next()) {
                                        videoTitle = rs.getString("videoTitle");
                                        videoID= rs.getInt("videoID");
                                        System.out.println("videoID : "+videoID+", videoTitle : "+videoTitle);
                                    }
                                }else {
                                    System.out.println("There is no video named : "+videoTitle);
                                    continue;
                                }
                                System.out.println("input the number of the video that you want to add");
                                int addedVideo = s.nextInt();
                                String addedVideostr = Integer.toString(addedVideo);
                                addedVideostr="'"+addedVideostr+"'";
                                stmt.executeUpdate("insert into construct(c_listID, c_videoID) values("+listIDstr+","+addedVideostr+")");
                                stmt.executeUpdate("update playlist set numOfVideo=numOfVideo+1 where listID="+listIDstr);
                                System.out.println("videoID : "+videoTitle+" added to playlistID : "+listIDstr);
                            }
                            else if(cmdNum==1) { //delete video
                                rs=stmt.executeQuery("Select videoTitle,videoID,upload_date from video as v where v.videoID In " +
                                        "(select c.c_videoID from construct as c where c_listID="+listIDstr+")");
                                int cnt=0;
                                if(rs.next()) {
                                    cnt++;
                                    videoID=rs.getInt("videoID");
                                    String videoTitle = rs.getString("videoTitle");
                                    Date upload_date=rs.getDate("upload_date");
                                    System.out.println("#videoID : "+videoID+" videoTitle : "+videoTitle+"upload_date : "+upload_date);
                                    while(rs.next()) {
                                        cnt++;
                                        videoID=rs.getInt("videoID");
                                        videoTitle = rs.getString("videoTitle");
                                        upload_date=rs.getDate("upload_date");
                                        System.out.println("#videoID : "+videoID+" videoTitle : "+videoTitle+" upload_date : "+upload_date);
                                    }
                                }else {
                                    System.out.println("this playlist is empty playlist");
                                    continue;
                                }
                                System.out.println("input the number of the video "+ " you want to delete from this playlist");
                                int deleteNum = s.nextInt();
                                stmt.executeUpdate("delete from construct where c_listID="+listIDstr+" and c_videoID="+deleteNum);
                                stmt.executeUpdate("update playlist set numOfVideo=numOfVideo-1 where listID="+listIDstr);
                                System.out.println("Deleting Complete!");
                                break;
                            }
                            break;
                        case 6:// select a playlist and show video list or watch video.
                            rs=stmt.executeQuery("Select listID,listName from playlist where Createrid="+userIDstr);
                            listID=0;
                            listIDstr="";
                            System.out.println("<Your playlists>");
                            if(rs.next()) {
                                listID=rs.getInt("listID");
                                listIDstr=Integer.toString(listID);
                                listIDstr="'"+listIDstr+"'";
                                listname = rs.getString("listName");
                                listname="'"+listname+"'";
                                System.out.println("listID : "+listID+", listName : "+listname);
                                while(rs.next()) {
                                    listID=rs.getInt("listID");
                                    listname = rs.getString("listName");
                                    listname="'"+listname+"'";
                                    System.out.println("listID : "+listID+", listName : "+listname);
                                }
                            }else {
                                System.out.println("--You don't have any playlists--");
                                continue;
                            }
                            System.out.println("Select the playlist you want to enter");
                            listID = s.nextInt();
                            listIDstr = Integer.toString(listID);
                            listIDstr="'"+listIDstr+"'";
                            rs=stmt.executeQuery("Select videoTitle,videoID,upload_date from video as v where v.videoID In " +
                                    "(select c.c_videoID from construct as c where c_listID="+listIDstr+")");
                            System.out.println("Your videos in playlist are...");
                            if (!rs.next()) {
                                System.out.println("empty! Insert video in your playlist!");
                                break;
                            }
                            while(rs.next()) {
                                String Title = rs.getString("videoTitle");
                                videoID = rs.getInt("videoID");
                                Date upload_date = rs.getDate("upload_date");
                                System.out.println("#videoID : "+videoID+" videoTitle : "+Title+" upload_date : "+upload_date);
                            }
                            System.out.println("Select videoID you want to watch or enter 0 to quit");
                            videoID = s.nextInt();
                            if (videoID==0) {
                                continue;
                            }
                            else {
                                rs = stmt.executeQuery("Select videoTitle,videoID,upload_date from video WHERE videoID="+videoID);
                                if(rs.next()){
                                    String Title = rs.getString("videoTitle");
                                    videoID = rs.getInt("videoID");
                                    videoIDstr = Integer.toString(videoID);
                                    videoIDstr = "'"+videoIDstr+"'";
                                    Date upload_date = rs.getDate("upload_date");
                                    System.out.println("You " + " watched videoID: " + videoID + " videoTitle: " + Title + " uploaded on : "+upload_date);
                                    stmt.executeUpdate("update video set views=views+1 where videoID="+videoIDstr);
                                    rs = stmt.executeQuery("SELECT * FROM watch WHERE w_videoID=" + videoIDstr + "and w_userID=" + userIDstr);
                                    if (!rs.next()) {
                                        stmt.executeUpdate("INSERT into watch(w_userID,w_videoID,watch_num) values("+userIDstr+","+videoIDstr+",'1')");
                                    } else {
                                        stmt.executeUpdate("UPDATE watch set watch_num=watch_num+1 WHERE w_videoID=" + videoIDstr + "and w_userID=" + userIDstr);
                                    }
                                }

                            }
                            break;
                        case 7:// delete the playlist
                            rs=stmt.executeQuery("Select listName,listID from playlist where Createrid="+userIDstr);
                            if(rs.next()) {
                                System.out.println("<Your playlists>");
                                listname=rs.getString("listname");
                                listID = rs.getInt("listID");
                                System.out.println("listID : "+listID+", listName : "+listname);
                                while(rs.next()) {
                                    listname = rs.getString("listName");
                                    listID = rs.getInt("listID");
                                    System.out.println("listID : "+listID+", listName : "+listname);
                                }
                            }else {
                                System.out.println("You don't have any playlist");
                                continue;
                            }
                            System.out.println("input a listID of the playlist you want to delete");
                            int delete = s.nextInt();
                            stmt.executeUpdate("Delete from construct where c_listID="+delete);
                            stmt.executeUpdate("Delete from playlist where listID="+delete);
                            System.out.println("Deleting Complete!");
                            break;
                        case 8:
                            continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
