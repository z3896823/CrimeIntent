package org.zyb.criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/16
 *     desc   : model类
 *     version: 1.0
 * </pre>
 */

public class Crime {

    private UUID id;//该id只读，仅用作区分不同对象
    private Date date;//该属性也只读，在对象生成的时候自动生成并赋值
    private String title;
    private boolean isSolved;

    public Crime() {
        this.id = UUID.randomUUID();
        this.date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public boolean getSolved() {
        return isSolved;
    }
}
