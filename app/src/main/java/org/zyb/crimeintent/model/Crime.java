//package org.zyb.crimeintent.model;
//
//import org.zyb.crimeintent.util.Utility;
//
//import java.util.Date;
//import java.util.UUID;
//
///**
// * <pre>
// *     author : zyb
// *     e-mail : hbdxzyb@hotmail.com
// *     time   : 2017/03/16
// *     desc   : model类
// *     version: 1.0
// * </pre>
// */
//
//public class Crime {
//
//    private UUID uuid;
//    private String date;
//    private String title;
//    private boolean isSolved;
//
//    public Crime() {
//        this.uuid = UUID.randomUUID();
//        this.date = Utility.dateToString(new Date());
//    }
//
//    public void setUuid(UUID uuid){
//        this.uuid = uuid;
//    }
//
//    public UUID getUuid() {
//        return uuid;
//    }
//
//    public void setDate(String date){
//        this.date = date;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setSolved(boolean solved) {
//        isSolved = solved;
//    }
//
//    public boolean getSolved() {
//        return isSolved;
//    }
//}
