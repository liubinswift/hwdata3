package org.jmask.web.exception;

/**
 * <p>Title: WebӦ���쳣��</p>
 *
 * <p>Description: ����WebӦ�ó�����쳣���Դ���Ϊ����</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: ��ʯ</p>
 *
 * @author �¸�
 * @version 1.0
 */
public class WebAppException extends Exception {
    public WebAppException() {
        super();
    }

    public WebAppException(String message) {
        super(message);
    }

    public WebAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebAppException(Throwable cause) {
        super(cause);
    }
}
