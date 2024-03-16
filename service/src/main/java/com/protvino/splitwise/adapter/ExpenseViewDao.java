package com.protvino.splitwise.adapter;

import java.util.List;

public interface ExpenseViewDao {

    List<ExpenseView> search(ExpenseSearchRequesr request);
}
