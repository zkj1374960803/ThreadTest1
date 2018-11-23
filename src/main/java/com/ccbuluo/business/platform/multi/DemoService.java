package com.ccbuluo.business.platform.multi;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dao.BizServiceLabelDao;
import com.ccbuluo.business.platform.label.service.LabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 损益单保存服务
 */
@Service
public class DemoService implements ISaveService<BizServiceLabel> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExecTask.class);
    @Resource(name = "myLabelServiceImpl")
    public LabelService labelServiceImpl;
    @Autowired
    BizServiceLabelDao bizServiceLabelDao;

    /**
     * 业务保存
     * @param list
     */
    public void save(List<BizServiceLabel> list){
        if(list.size() == 1){
            throw new RuntimeException();
        }
        for(BizServiceLabel item :list){
            bizServiceLabelDao.saveEntity(item);
        }
    }
    /**
     * 批量保存事件
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer batchSave(List<BizServiceLabel> list, MultiEndFlag endFlag, UUID threadId) throws Exception {
        int result = 0;
        try{
            //业务操作
            save(list);
            result = 1;
            //进行waitForEnd 操作，是为了确保所有的线程都最终通知同步协作标志
            endFlag.waitForEnd(threadId ,result);
            //其他线程异常手工回滚
            if(result==1 && !endFlag.isAllSuccess()){
                String message = "子线程未全部执行成功，对线程["+Thread.currentThread().getName()+"]进行回滚";
                throw new Exception(message);
            }
            return result;
        }catch (Exception ex){
            logger.error(ex.toString());
            if(result ==0){
                //本身线程异常抛出异常，通知已经做完（判断是为了防止 与 try块中的通知重复）
                endFlag.waitForEnd(threadId ,result);
            }
            throw ex;
        }
    }

//    /**
//     * 调用示例
//     * @param args
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ArrayList<NoteCheckBalance> noteCheckBalances = new ArrayList<NoteCheckBalance>();
//        for (int i = 0; i < 30; i++) {
//            noteCheckBalances.add(new NoteCheckBalance("张三" + "【"+ i +"】"));
//        }
////        Object demoService = SpringUtil.getBean("multi.DemoService");
//        //调用示例
//        DemoService demoService1 = new DemoService();
//        demoService1.noteCheckBalanceMapper = new NoteCheckBalanceMapper();
//
//        MultiExecutor.exec(demoService1, noteCheckBalances,10);
//    }
}

