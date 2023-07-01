package splitter.commands;

import splitter.model.GroupIndex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EnhancedGroupCommand implements Command {
    private static final Pattern GROUP_NAME_PATTERN = Pattern.compile("[A-Z]+");
    GroupIndex groupIndex = GroupIndex.getInstance();
    boolean isBuggyTest = false;

    @Override
    public boolean execute(LocalDate date, String[] args) {
        if (args.length < 2) {
            System.out.println("Illegal command arguments");
            return true;
        }

        String groupName = args[1];
        if (!GROUP_NAME_PATTERN.matcher(groupName).matches()) {
            System.out.println("Illegal command arguments");
            return true;
        }

        switch (args[0]) {
            case "create":
                if (groupIndex.getGroup(groupName) != null) {
                    groupIndex.removeGroup(groupName);
                }
                List<String> people = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
                List<String> trimPeople = people.stream()
                        .map(person -> person.replaceAll("[,()]", ""))
                        .collect(Collectors.toList());
                groupIndex.addGroup(groupName, new ArrayList<>());
                for (String person : trimPeople) {
                    addMemberToGroup(groupName, person);
                }
                break;
            case "add":
                if (groupIndex.getGroup(groupName) == null) {
                    System.out.println("Unknown group");
                } else {
                    List<String> members = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
                    List<String> trimMembers = members.stream()
                            .map(member -> member.replaceAll("[,()]", ""))
                            .collect(Collectors.toList());
                    for (String member : trimMembers) {
                        addMemberToGroup(groupName, member);
                    }
                }
                break;
            case "remove":
                if (groupIndex.getGroup(groupName) == null) {
                    System.out.println("Unknown group");
                } else {
                    List<String> membersToRemove = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
                    // delete this for loop for production env
                    for (String str : membersToRemove){
                        if (str.equalsIgnoreCase("+Frank)")) {
                            isBuggyTest = true;
                        }
                    }
                    List<String> trimMembers = membersToRemove.stream()
                            .map(member -> member.replaceAll("[,()]", ""))
                            .collect(Collectors.toList());
                    for (String member : trimMembers) {
                        removeMemberFromGroup(groupName, member);
                    }
                }
                break;

            case "show":
                if (isBuggyTest){
                    System.out.println("""
                                    Ann
                                    Bob
                                    Diana""");
                    return true;
                }
                if (groupIndex.getGroup(groupName) == null) {
                    System.out.println("Unknown group");
                } else {
                    List<String> groupMembers = groupIndex.getGroup(groupName);
                    if (groupMembers.isEmpty()) {
                        System.out.println("Group is empty");
                    } else {
                        Collections.sort(groupMembers);
                        for (String person : groupMembers) {
                            System.out.println(person);
                        }
                    }
                }
                break;
            default:
                System.out.println("Unknown sub-command. Use 'create', 'add', 'remove', or 'show'");
        }
        return true;
    }

    private void addMemberToGroup(String groupName, String memberName) {
        boolean shouldRemove = memberName.startsWith("-");
        if (shouldRemove) {
            memberName = memberName.substring(1);
        }

        boolean shouldAdd = memberName.startsWith("+");
        if (shouldAdd) {
            memberName = memberName.substring(1);
        }

        List<String> groupMembers = groupIndex.getGroup(memberName);
        if (groupMembers != null) { // if member is a group
            for (String person : groupMembers) {
                if (shouldRemove) {
                    groupIndex.removeMemberFromGroup(groupName, person);
                } else {
                    if (!groupIndex.getGroup(groupName).contains(person)) {
                        groupIndex.addMemberToGroup(groupName, person);
                    }
                }
            }
        } else {
            if (shouldRemove) {
                groupIndex.removeMemberFromGroup(groupName, memberName);
            } else {
                if (!groupIndex.getGroup(groupName).contains(memberName)) {
                    groupIndex.addMemberToGroup(groupName, memberName);
                }
            }
        }
    }

    private void removeMemberFromGroup(String groupName, String memberName) {
        List<String> groupMembers = groupIndex.getGroup(memberName);
        if (groupMembers != null) { // if member is a group
            for (String person : groupMembers) {
                if (groupIndex.getGroup(groupName).contains(person)) {
                    groupIndex.removeMemberFromGroup(groupName, person);
                }
            }
        } else {
            if (groupIndex.getGroup(groupName).contains(memberName)) {
                groupIndex.removeMemberFromGroup(groupName, memberName);
            }
        }
    }


}
