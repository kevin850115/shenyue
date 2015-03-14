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
 * 所有订单处理器的抽象类
 * @author leconte
 *
 */
public abstract class TframeAbstractHandler<Param, Result extends ProcessResultDO, Model extends DataModel> implements IHandler<Param, Result, Model> {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected TframeSystemConfig systemConfig;
	/**
	 * 数据持久化方法
	 */
	abstract public void save2db(Model dataModel,Result result) throws DAOException;

	@Override
	public void save(final Model dataModel,final Result result) throws Exception{
		TransactionTemplate txTemplate = this.systemConfig.getTxTemplate();
		if ( txTemplate == null ){
			throw new Exception("事务模板为空，无法保存数据库");
		}
		Exception e = (Exception) txTemplate
		.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				try {
					TxOrderAnalyse.setAnalyzeOn();
					save2db(dataModel,result);
				} catch (DAOException e) { // 出现错误，回滚事务
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
