package com.hhnz.impl.cfcrm.service.cfcrm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hhnz.api.cfcrm.model.cfcrm.PaymentCollection;
import com.hhnz.api.cfcrm.service.cfcrm.IPaymentCollectionService;

@Service("paymentCollectionService")
@Transactional("cfcrmTransactionManager")
public class PaymentCollectionServiceImpl extends ServiceImpl<PaymentCollection> implements IPaymentCollectionService {
}