package com.tradeshift.models.task;

import com.tradeshift.models.W9;

public class W9ValidationTask extends Task {
    private W9 _w9;

    public W9 getW9() {
        return _w9;
    }

    public void setW9(W9 w9) {
        _w9 = w9;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.W9;
    }
}
