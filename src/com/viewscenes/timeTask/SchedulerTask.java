package com.viewscenes.timeTask;

import java.util.TimerTask;

public abstract class SchedulerTask implements Runnable 
{//�����������ϵ���ִ�е�����

    final Object lock = new Object();

    int state = VIRGIN;//����״̬
    static final int VIRGIN = 0;
    static final int SCHEDULED = 1;
    static final int CANCELLED = 2;

    TimerTask timerTask;//�ײ�Ķ�ʱ������
    protected SchedulerTask() 
    {
    }
    public abstract void run();//��������ִ�еľ�������
    public boolean cancel() 
    {
        synchronized(lock) 
        {
            if (timerTask != null) 
            {
                timerTask.cancel();//ȡ������
            }
            boolean result = (state == SCHEDULED);//�����Ѿ�������ִ��
            state = CANCELLED;//��������״̬Ϊ��ȡ����
            return result;
        }
    }
    public long scheduledExecutionTime()
    {
        synchronized(lock) 
        {
            return timerTask == null ? 0 : timerTask.scheduledExecutionTime();//����ִ��ʱ��
        }
    }

}