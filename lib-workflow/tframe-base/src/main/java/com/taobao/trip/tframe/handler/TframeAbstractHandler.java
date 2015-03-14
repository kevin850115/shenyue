package com.taobao.trip.tframe.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.taobao.common.dao.persistence.exception.DAOException;
import com.taobao.trip.tframe.config.TframeSystemConfig;
import com.taobao.trip.tframe.process.DataModel;
import com.taobao.trip.tframe.process.IHandler;
import com.taobao.trip.tframe.process.ProcessResultDO;
import com.taobao.trip.tframe.tx.TxOrderAnalyse;

/**
 * ���ж����������ĳ�����
 * @author leconte
 *
 */
public abstract class TframeAbstractHandler<Param, Result extends ProcessResultDO, Model extends DataModel> implements IHandler<Param, Result, Model> {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected TframeSystemConfig systemConfig;
	/**
	 * ���ݳ־û�����
	 */
	abstract public void save2db(Model dataModel,Result result) throws DAOException;

	@Override
	public void save(final Model dataModel,final Result result) throws Exception{
		TransactionTemplate txTemplate = this.systemConfig.getTxTemplate();
		if ( txTemplate == null ){
			throw new Exception("����ģ��Ϊ�գ��޷��������ݿ�");
		}
		Exception e = (Exception) txTemplate
		.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				try {
					TxOrderAnalyse.setAnalyzeOn();
					save2db(dataModel,result);
				} catch (DAOException e) { // ���ִ��󣬻ع�����
					status.setRollbackOnly();
					return e;
				}finally{
					TxOrderAnalyse.setAnalyzeOff();
				}
				return null;
			}
		});
		if (null != e) {
			throw e;
		}	
	}

}
