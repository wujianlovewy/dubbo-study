package cn.itcast.dubbo.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class IDUtil implements InitializingBean,ApplicationContextAware{

	private String macID = null;
	
	private static final String ID_ROOT_PATH = "/IDRoot";
	private static final String WORK_ID_PREFIX = "E:\\usr\\core";
	private static final String WORK_ID_FILE_NAME = "\\work.id"; 
	private String WORK_ID_FILE;
	private static final int WORK_ID_LEN = 2;
	
	private ApplicationContext context;
	
	@Autowired
	private ZKClientUtil zkClientUtil;
	
	public String getWorkID() throws IOException{
		File file = new File(WORK_ID_FILE);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if(!file.exists()){
			file.createNewFile();
		}
	  return FileUtils.readFileToString(file);
	}
	
	private String getMacId(){
		String workId = null;
		try {
			workId = this.getWorkID();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(null==workId || "".equals(workId) || "0".equals(workId)){
			System.out.println("开始生成workID===>");
			ZkClient zkClient = zkClientUtil.connect();
			//创建zk根节点
			if(!zkClient.exists(ID_ROOT_PATH)){
				zkClient.createPersistent(ID_ROOT_PATH, false);
			}
			String zkPerSeq = zkClient.createPersistentSequential(ID_ROOT_PATH+"/ID", new Date());
			workId = zkPerSeq.substring(zkPerSeq.length()-WORK_ID_LEN);
			return workId;
		}
		System.out.println("成功读取本地文件缓存的workID===>");
		return workId;
	}
	
	public void afterPropertiesSet() throws Exception {
		this.macID = this.getMacId();
		FileUtils.writeStringToFile(new File(WORK_ID_FILE), this.macID, false);
		System.out.println("生成服务器机器码: "+this.macID);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				System.out.println("销毁上线机器码");
			}
		});
	}

	public String getMacID() {
		return macID;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
		String webId = this.context.getId();
		this.WORK_ID_FILE = WORK_ID_PREFIX + webId.substring(webId.indexOf(":")+1) + WORK_ID_FILE_NAME;
	}
	
	

}
