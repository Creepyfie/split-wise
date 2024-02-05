package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.GroupDao;
import com.protvino.splitwise.domain.request.EditGroupRequest;
import com.protvino.splitwise.domain.value.Group;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("DAO")
abstract class GroupDaoTestCases {
    abstract GroupDao getDao();

    GroupDao groupDAO = getDao();

    @Test
    void create__when_does_not_exist() {
        // Arrange
        String name = "GroupName";

        // Act
        Long actualId = groupDAO.create(new EditGroupRequest(name));
        Group actualResult = groupDAO.findById(actualId);

        // Assert
        Group expectedResult = new Group(actualId, name);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void update__when_exists() {
        // Arrange
        String name = "GroupName";
        Long actualId = groupDAO.create(new EditGroupRequest(name));

        // Act
        String updateName = "updatedGroupName";

        groupDAO.update(actualId, new EditGroupRequest(updateName));

        Group actualResult = groupDAO.findById(actualId);

        // Assert
        Group expectedResult = new Group(actualId, updateName);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }
    @Test
    void update__when_does_not_exist() {
        // Arrange
        // Act
        String updateName = "updatedGroupName";
        groupDAO.update(123L, new EditGroupRequest(updateName));

        Group actualResult = groupDAO.findById(123L);

        // Assert
        assertThat(actualResult)
                .isNull();
    }

    @Test
    void find_by_id__when_exists() {
        // Arrange
        String groupName = "GroupName";
        Long actualId = groupDAO.create(new EditGroupRequest(groupName));

        // Act
        Group actualResult = groupDAO.findById(actualId);

        // Assert
        Group expectedResult = new Group(actualId, groupName);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }
    @Test
    void find_by_id__when_does_not_exist() {
        // Arrange
        String groupName = "GroupName";
        groupDAO.create(new EditGroupRequest(groupName));

        // Act
        Group actualResult = groupDAO.findById(123L);

        // Assert
        assertThat(actualResult)
                .isNull();
    }
}
