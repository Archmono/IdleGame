package com.example.alucardc.idlegame;

/**
 * Created by AlucardC on 2017/12/6.
 */

public class Player {
    public MyPlayerStatus[] playerStatus;
    public MyPlayerInventory[] playerInventory;
    public MyPlayerMobsCollection[] playerMobsCollection;
    public MyPlayerSceneProgress[] playerSceneProgress;
}

class MyPlayerStatus
{
    public String playerID;
    public int playerMaxHP;
    public int playerCurrentHP;
    public int playerATK;
}

class MyPlayerInventory
{
    public int
        i100101,i100102,i100103,
        i100201,i100202,i100203,
        i100301,i100302,i100303,
        i110101,i110102,
        i200101,i200102,i200103,
        i200201,i200202,i200203,
        i200301,i200302,i200303,
        i210101,i210102,
        i910101,i910102;
}

class MyPlayerMobsCollection
{
    public mobs1001 m1001;
    public mobs1002 m1002;
    public mobs1003 m1003;
    public mobs1101 m1101;
    public mobs2001 m2001;
    public mobs2002 m2002;
    public mobs2003 m2003;
    public mobs2101 m2101;
    public mobs9101 m9101;
}

class mobs1001{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs1002{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs1003{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs1101{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs2001{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs2002{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs2003{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs2101{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class mobs9101{
    public int image;
    public int name;
    public int atk;
    public int mobsBio;
    public int loot1;
    public int loot2;
    public int loot3;
}
class MyPlayerSceneProgress{
    public boolean Scene_1;
    public boolean Scene_2;
}