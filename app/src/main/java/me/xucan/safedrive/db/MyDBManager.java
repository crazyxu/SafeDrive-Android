package me.xucan.safedrive.db;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

public class MyDBManager
{
	private static MyDBManager myDBManager;
	private static DbManager dbManager;
	
	//数据库配置
	private static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("snap.db")//创建数据库的名称
            .setDbVersion(1)//数据库版本号
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                  
                }
            });
	
	/**
	 * 获取实例
	 */
	public static MyDBManager getInstance(){
		if (myDBManager == null) {
			synchronized (MyDBManager.class) {
				if (myDBManager == null) {
					myDBManager = new MyDBManager();
					dbManager = x.getDb(daoConfig);
				}
			}
		}
		return myDBManager;
	}

    
    /**
     * 清除数据
     */
    public void deleteAll(Class<?> clazz) {
		try {
			dbManager.delete(clazz);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
    
    
    

}
