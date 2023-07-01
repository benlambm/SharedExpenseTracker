package splitter.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupIndex {
    private static GroupIndex instance;
    private final Map<String, List<String>> groups;

    private GroupIndex() {
        this.groups = new LinkedHashMap<>();
    }

    public static GroupIndex getInstance() {
        if (instance == null) {
            instance = new GroupIndex();
        }
        return instance;
    }

    public List<String> getGroup(String groupName) {
        return groups.get(groupName);
    }

    public boolean isEmptyGroup(String groupName) {
        List<String> group = groups.get(groupName);
        if (group == null || groups.isEmpty()) {
            return true;
        }
        return false;
    }

    public void addGroup(String groupName, List<String> members) {
        groups.put(groupName, members);
    }

    public void addMemberToGroup(String groupName, String member) {
        List<String> group = groups.get(groupName);
        if (group != null) {
            group.add(member);
        }
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }

    public void removeMemberFromGroup(String groupName, String member) {
        List<String> group = groups.get(groupName);
        if (group != null) {
            group.remove(member);
        }
    }
}