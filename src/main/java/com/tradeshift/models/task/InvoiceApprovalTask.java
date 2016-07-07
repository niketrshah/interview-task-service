package com.tradeshift.models.task;

import com.tradeshift.models.Invoice;

public class InvoiceApprovalTask extends Task {
    private Invoice _invoice;

    public Invoice getInvoice() {
        return _invoice;
    }

    public void setInvoice(Invoice invoice) {
        _invoice = invoice;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.INVOICE;
    }
}
