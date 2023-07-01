package splitter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Groups {
    private final Map<String, List<String>> groups;

    public Groups() {
        this.groups = new HashMap<>();
    }

    public List<String> getGroup(String groupName) {
        return groups.get(groupName);
    }

    public void addGroup(String groupName) {
        groups.put(groupName, new ArrayList<>());
    }

    public void addMemberToGroup(String groupName, String member) {
        List<String> group = groups.get(groupName);
        if (group != null) {
            group.add(member);
        }
    }

    // ... other methods to manipulate 'groups' as needed
}

