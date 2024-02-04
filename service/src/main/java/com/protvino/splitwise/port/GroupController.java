package com.protvino.splitwise.port;

import com.protvino.splitwise.port.dto.GroupDto;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/group")
public class GroupController {

    Map<Long, GroupDto> groups = new HashMap<>();

    @GetMapping
    public GroupDto getGroup(long id){return groups.get(id);}

    @PostMapping
    public Long createGroup()
    {
        GroupDto groupDto = GroupDto.builder()
                .id(ThreadLocalRandom.current().nextLong())
                .name("groupName_" + ThreadLocalRandom.current().nextInt(1000))
                .build();
        groups.put(groupDto.getId(),groupDto);
        return groupDto.getId();
    }

}
