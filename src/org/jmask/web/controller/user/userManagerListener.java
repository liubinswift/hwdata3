package  org.jmask.web.controller.user;

public class userManagerListener extends Thread {
	private userManager um;
	public userManagerListener(userManager um){
		super();
		this.um=um;
	}
	public void run(){
		System.out.println("�첽��������");
		while(true){
			try {
				sleep(30*60*1000);//��Сʱ����һ��;
				um.processFlag=true;
				sleep(500);//��֤�Ѿ���ִ�е�ִ�����;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//�����û���Ϣ
			System.out.println("��ʼ�����û���Ϣ");
			java.util.Date sDate = new java.util.Date();
		    long sTime = sDate.getTime();
			
		    um.processFlag=false;
			synchronized(um){
				System.out.println("�û���Ϣ�������");
				um.notifyAll();
			}
		}
	}
}
