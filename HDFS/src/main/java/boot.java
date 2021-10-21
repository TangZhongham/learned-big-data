public class boot {
    private static String result = "";
    public static void main(String[] args) throws Exception {
        try {
            if(args[0] != null && !"".equals(args[0]) && args.length > 0){
                if("upload".equals(args[0])){
                    HDFSOperation.upload(args);
                }else if ("delete".equals(args[0])){
                    HDFSOperation.deleteFile(args);
                }else if ("mkdir".equals(args[0])){
                    HDFSOperation.mkdirDir(args);
                }else if("list".equals(args[0])){
                    HDFSOperation.listDir(args);
                }else if("help".equals(args[0])){
                    System.out.println("Usage: java -jar hdfsclient-1.0-SNAPSHOT.jar [OPTION]... \n");
                    System.out.println("Provide hdfs client operations.\n");
                    System.out.println("     delete   delete file in hdfs filesystem,require filename.\n");
                    System.out.println("     mkdir    mkdir directory in filesystem,require directory name.\n");
                    System.out.println("     upload   upload require specify src filename && dst dirname.\n");
                    System.out.println("     list     show all file&directory of specify directory.\n");
                }else {
                    System.out.println("\n");
                    System.out.println("Usage: java -jar hdfsclient-1.0-SNAPSHOT.jar [OPTION]... \n");
                    System.out.println("     help     show all operations.\n");
                    System.exit(1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
