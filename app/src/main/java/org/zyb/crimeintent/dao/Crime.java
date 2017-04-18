package org.zyb.crimeintent.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/04/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

@Entity
public class Crime {

    @Id(autoincrement = true)
    private Long id;
    @Property
    private String date;
    private String title;
    private boolean isSolved;



    @Generated(hash = 1673964492)
    public Crime(Long id, String date, String title, boolean isSolved) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.isSolved = isSolved;
    }
    @Generated(hash = 947324445)
    public Crime() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean getIsSolved() {
        return this.isSolved;
    }
    public void setIsSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }
}
