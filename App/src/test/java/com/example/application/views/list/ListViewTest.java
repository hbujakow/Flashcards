package com.example.application.views.list;

import com.example.application.data.entity.Slowko;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ListViewTest {

    @Autowired
    private ListView listView;

    @Test
    public void formShownWhenContactSelected() {
        Grid<Slowko> grid = listView.grid;
        Slowko firstSlowko = getFirstItem(grid);

        WordForm form = listView.form;

        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstSlowko);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstSlowko.getWord(), form.word.getValue());
    }

    private Slowko getFirstItem(Grid<Slowko> grid) {
        return( (ListDataProvider<Slowko>) grid.getDataProvider()).getItems().iterator().next();
    }
}
