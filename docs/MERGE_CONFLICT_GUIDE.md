# Merge Conflict Resolution Guide

## 📋 Overview
This guide demonstrates how to intentionally create, identify, and resolve merge conflicts in Git, following industry best practices for conflict resolution.

---

## 🎯 Part 1: Understanding Merge Conflicts

### What is a Merge Conflict?

A merge conflict occurs when:
1. Two branches modify the **same lines** in the **same file**
2. Git cannot automatically determine which changes to keep
3. Manual intervention is required to resolve the conflict

### When Do Conflicts Occur?

```
main:        A---B---C---D
                  \
testing:           B'---C'---D'

Conflict if: Line 10 in B != Line 10 in B' (same file)
```

### Types of Conflicts

1. **Content Conflict**: Same lines edited differently
2. **Delete-Modify Conflict**: One branch deletes, other modifies
3. **Rename Conflict**: Same file renamed differently

---

## 🔧 Part 2: Intentionally Creating a Merge Conflict (Demo)

### Scenario: Modify StudentService in Both Branches

#### Step 1: Make Changes in Main Branch

```powershell
# Switch to main branch
git checkout main

# Pull latest changes
git pull origin main
```

**Edit the file**: `src/main/java/com/example/webapp/service/StudentService.java`

Add this method after `saveStudent()`:

```java
/**
 * Get total count of students
 * @return total number of students
 */
public long getTotalStudentCount() {
    return studentRepository.count();
}
```

**Commit the change**:
```powershell
git add src/main/java/com/example/webapp/service/StudentService.java
git commit -m "feat: add getTotalStudentCount method to main"
git push origin main
```

#### Step 2: Make Conflicting Changes in Testing Branch

```powershell
# Switch to testing branch
git checkout testing/unit-integration-tests

# Make sure you're on the version BEFORE merging main
git log --oneline -5
```

**Edit the SAME file**: `src/main/java/com/example/webapp/service/StudentService.java`

Add this method at the SAME location (after `saveStudent()`):

```java
/**
 * Calculate total number of students in system
 * @return count of all students
 */
public long calculateStudentTotal() {
    return studentRepository.count();
}
```

**Commit the change**:
```powershell
git add src/main/java/com/example/webapp/service/StudentService.java
git commit -m "feat: add calculateStudentTotal method to testing"
git push origin testing/unit-integration-tests
```

#### Step 3: Attempt to Merge (Trigger Conflict)

```powershell
# Stay on testing branch
git checkout testing/unit-integration-tests

# Attempt to merge main
git merge main
```

#### Expected Output (Conflict!):
```
Auto-merging src/main/java/com/example/webapp/service/StudentService.java
CONFLICT (content): Merge conflict in src/main/java/com/example/webapp/service/StudentService.java
Automatic merge failed; fix conflicts and then commit the result.
```

✅ **Success!** We've created a merge conflict.

---

## 🔍 Part 3: Identifying Merge Conflicts

### Check Status

```powershell
git status
```

**Output**:
```
On branch testing/unit-integration-tests
You have unmerged paths.
  (fix conflicts and run "git commit")
  (use "git merge --abort" to abort the merge)

Unmerged paths:
  (use "git add <file>..." to mark resolution)
        both modified:   src/main/java/com/example/webapp/service/StudentService.java

no changes added to commit (use "git add" and/or "git commit -a")
```

### View Conflict Details

```powershell
git diff
```

Or open the file in VS Code (recommended).

### Conflict Markers in File

Open `StudentService.java` and you'll see:

```java
public void deleteStudent(Long id) {
    studentRepository.deleteById(id);
}

<<<<<<< HEAD (Current Change - testing branch)
/**
 * Calculate total number of students in system
 * @return count of all students
 */
public long calculateStudentTotal() {
    return studentRepository.count();
}
=======
/**
 * Get total count of students
 * @return total number of students
 */
public long getTotalStudentCount() {
    return studentRepository.count();
}
>>>>>>> main (Incoming Change - main branch)
```

### Understanding Conflict Markers

| Marker | Meaning |
|--------|---------|
| `<<<<<<< HEAD` | Start of YOUR changes (current branch) |
| `=======` | Separator between changes |
| `>>>>>>> main` | End of INCOMING changes (branch being merged) |

---

## ✅ Part 4: Resolving Conflicts Locally

### Method 1: Manual Resolution in Editor (Recommended)

#### Step 1: Open File in VS Code

VS Code will show conflict markers with options:
- **Accept Current Change** (testing branch version)
- **Accept Incoming Change** (main branch version)
- **Accept Both Changes**
- **Compare Changes**

#### Step 2: Choose Resolution Strategy

**Option A: Accept Both Methods (Best Choice)**

Remove conflict markers and keep both:

```java
public void deleteStudent(Long id) {
    studentRepository.deleteById(id);
}

/**
 * Get total count of students
 * @return total number of students
 */
public long getTotalStudentCount() {
    return studentRepository.count();
}

/**
 * Calculate total number of students in system (alias method)
 * @return count of all students
 * @deprecated Use getTotalStudentCount() instead
 */
@Deprecated
public long calculateStudentTotal() {
    return getTotalStudentCount();
}
```

**Option B: Keep Only One**

Choose the better method name and delete the other:

```java
public void deleteStudent(Long id) {
    studentRepository.deleteById(id);
}

/**
 * Get total count of students
 * @return total number of students
 */
public long getTotalStudentCount() {
    return studentRepository.count();
}
```

#### Step 3: Save the File

Save `StudentService.java` after resolving conflicts.

#### Step 4: Mark as Resolved

```powershell
# Stage the resolved file
git add src/main/java/com/example/webapp/service/StudentService.java

# Verify status
git status
```

**Output**:
```
On branch testing/unit-integration-tests
All conflicts fixed but you are still merging.
  (use "git commit" to conclude merge)

Changes to be committed:
        modified:   src/main/java/com/example/webapp/service/StudentService.java
```

#### Step 5: Complete the Merge

```powershell
# Commit the merge
git commit -m "merge: resolve conflict in StudentService - keep both methods"

# Push the merge
git push origin testing/unit-integration-tests
```

### Method 2: Using Git Command Line Tools

```powershell
# See conflict summary
git diff --name-only --diff-filter=U

# Use merge tool (opens graphical tool)
git mergetool

# Or use VS Code as merge tool
git config --global merge.tool vscode
git config --global mergetool.vscode.cmd 'code --wait "$MERGED"'
git mergetool
```

### Method 3: Abort and Start Over

If you make a mistake:

```powershell
# Abort the merge
git merge --abort

# Start fresh
git merge main
```

---

## 🌐 Part 5: Resolving Conflicts in GitHub UI

### When to Use GitHub UI Resolver

- Simple conflicts (few lines)
- Text-based changes
- No complex logic changes

### Step-by-Step Process

#### Step 1: Create Pull Request

```powershell
# Push testing branch
git push origin testing/unit-integration-tests
```

Go to GitHub → Create Pull Request

#### Step 2: GitHub Shows Conflict Warning

```
⚠️ This branch has conflicts that must be resolved
   Conflicting files:
   - src/main/java/com/example/webapp/service/StudentService.java

Resolve conflicts   Use command line
```

#### Step 3: Click "Resolve conflicts"

GitHub will open web editor showing:

```java
<<<<<<< testing/unit-integration-tests
public long calculateStudentTotal() {
    return studentRepository.count();
}
=======
public long getTotalStudentCount() {
    return studentRepository.count();
}
>>>>>>> main
```

#### Step 4: Edit in Web Editor

Remove conflict markers and edit as desired:

```java
public long getTotalStudentCount() {
    return studentRepository.count();
}
```

#### Step 5: Mark as Resolved

1. Click **Mark as resolved**
2. Click **Commit merge**
3. Commit message: `merge: resolve StudentService conflict`
4. Click **Commit**

✅ Conflict resolved in GitHub UI!

---

## 🔄 Part 6: Best Practices for Conflict Resolution

### Before Merging

```powershell
# Always pull latest changes first
git checkout testing/unit-integration-tests
git pull origin testing/unit-integration-tests

# Fetch main branch updates
git fetch origin main

# Check what will be merged
git log main..testing/unit-integration-tests
git log testing/unit-integration-tests..main

# Dry run (see conflicts without merging)
git merge --no-commit --no-ff main
# Then abort: git merge --abort
```

### During Resolution

1. ✅ **Understand both changes** before deciding
2. ✅ **Test after resolving** - ensure code still works
3. ✅ **Keep both if possible** - reconcile instead of choosing
4. ✅ **Communicate with team** - discuss with code author if unclear
5. ✅ **Run tests** - ensure no broken functionality

```powershell
# After resolving, test immediately
mvn clean test
```

6. ✅ **Check for logical conflicts** - not just syntax
7. ✅ **Review entire file** - conflicts might affect other parts
8. ✅ **Document resolution** - explain in commit message

### After Resolution

```powershell
# Verify no remaining conflicts
git status

# Run full test suite
mvn clean test

# Check for unintended changes
git diff HEAD~1

# Push and create PR
git push origin testing/unit-integration-tests
```

---

## 🚨 Part 7: Common Conflict Scenarios and Solutions

### Scenario 1: Both Branches Added Same Feature

**Problem**: Duplicate functionality

**Solution**:
- Keep the better implementation
- Or merge both with slight variations
- Document which is primary

**Example**:
```java
// CONFLICT: Both added logging
<<<<<<< HEAD
logger.info("Processing student: {}", id);
=======
log.debug("Student processing started: {}", id);
>>>>>>> main

// RESOLVED: Keep both at different levels
logger.info("Processing student: {}", id);
log.debug("Student processing started with ID: {}", id);
```

### Scenario 2: Formatting Differences

**Problem**: One branch reformatted code

**Solution**:
- Accept the version with better formatting
- Run formatter after merge
- Configure auto-formatter in .editorconfig

```powershell
# After resolving, reformat
mvn spring-javaformat:apply
```

### Scenario 3: Deleted vs Modified

**Problem**: One branch deleted method, other modified it

**Git Output**:
```
CONFLICT (modify/delete): StudentService.java deleted in main and modified in HEAD.
```

**Solution**:
- If deletion was intentional → keep deleted
- If modification is critical → keep modified
- Consider creating new method instead

```powershell
# Keep deleted version
git rm StudentService.java
git commit -m "merge: accept deletion of StudentService"

# Keep modified version
git add StudentService.java
git commit -m "merge: preserve modified StudentService"
```

### Scenario 4: Same Line Different Changes

**Problem**: Exact same line changed differently

```java
<<<<<<< HEAD
private static final int MAX_STUDENTS = 100;
=======
private static final int MAX_STUDENTS = 500;
>>>>>>> main
```

**Solution**:
- Discuss with team which value is correct
- Consider configuration property instead
- Document the decision

```java
// RESOLVED: Use higher limit with documentation
/**
 * Maximum number of students allowed
 * Increased from 100 to 500 to accommodate growth
 */
private static final int MAX_STUDENTS = 500;
```

---

## 🛠️ Part 8: Advanced Conflict Resolution

### Using Rebase Instead of Merge

**Benefits**:
- Linear history
- Cleaner commit graph
- Easier to track changes

```powershell
# Instead of merge, use rebase
git checkout testing/unit-integration-tests
git rebase main

# If conflicts occur
git status
# Fix conflicts in editor
git add <resolved-files>
git rebase --continue

# If too many conflicts
git rebase --abort
```

### Three-Way Merge Strategy

```powershell
# See all three versions
git show :1:StudentService.java > base.java    # Common ancestor
git show :2:StudentService.java > ours.java    # Current branch
git show :3:StudentService.java > theirs.java  # Incoming branch

# Compare and decide
code base.java ours.java theirs.java
```

### Using Merge Strategy Options

```powershell
# Prefer current branch changes
git merge -X ours main

# Prefer incoming changes
git merge -X theirs main

# Ignore whitespace changes
git merge -X ignore-space-change main
```

---

## 📊 Part 9: Merge Conflict Prevention Strategies

### 1. Frequent Integration

```powershell
# Merge main into your branch regularly
git checkout testing/unit-integration-tests
git merge main

# Do this daily or multiple times per day
```

### 2. Small, Focused Commits

```markdown
❌ Bad: giant commit with 50 file changes
✅ Good: small commits, one feature per commit
```

### 3. Communication

```markdown
✅ Tell team before modifying shared files
✅ Use feature flags for large changes
✅ Coordinate on Slack/Teams before big refactors
```

### 4. Code Ownership

```
CODEOWNERS file:
/src/main/java/com/example/webapp/service/  @team-backend
/src/main/java/com/example/webapp/controller/  @team-api
```

### 5. Branch Naming Strategy

```
feature/add-student-export    → New feature
bugfix/fix-student-validation → Bug fix
refactor/improve-service     → Code improvement
```

### 6. Pull Request Size Limits

```markdown
✅ Small PR: 50-200 lines
⚠️ Medium PR: 200-500 lines
❌ Large PR: 500+ lines (split it!)
```

---

## 🐛 Part 10: Troubleshooting

### Issue: "Cannot merge unrelated histories"

**Error**:
```
fatal: refusing to merge unrelated histories
```

**Solution**:
```powershell
git merge main --allow-unrelated-histories
```

### Issue: Lost work after conflict resolution

**Solution**:
```powershell
# Find lost commit
git reflog

# Restore it
git checkout <commit-hash>
git cherry-pick <commit-hash>
```

### Issue: Conflict in binary file

**Error**:
```
CONFLICT (content): Merge conflict in logo.png
```

**Solution**:
```powershell
# Choose version to keep
git checkout --ours logo.png    # Keep current
git checkout -- theirs logo.png  # Keep incoming

git add logo.png
git commit
```

### Issue: Too many conflicts

**Solution**:
```powershell
# Abort and use rebase instead
git merge --abort
git rebase -i main

# Or squash commits first
git rebase -i HEAD~5
```

---

## 📝 Part 11: Merge Conflict Checklist

### Before Merging
- [ ] Pull latest from origin
- [ ] Review changes in both branches
- [ ] Communicate with team
- [ ] Backup your work

### During Conflict Resolution
- [ ] Understand both changes
- [ ] Consider all affected code
- [ ] Test the resolution
- [ ] Run full test suite
- [ ] Check for logical issues
- [ ] Review final diff

### After Resolution
- [ ] All tests pass
- [ ] Code compiles
- [ ] No syntax errors
- [ ] Functionality preserved
- [ ] Documented resolution
- [ ] Pushed to remote

---

## 🎓 Part 12: Learning Exercise

### Exercise 1: Simple Conflict

```powershell
# Create conflict scenario
git checkout -b exercise-1
echo "Line 1 - Version A" > test.txt
git add test.txt
git commit -m "Add line A"

git checkout main
echo "Line 1 - Version B" > test.txt
git add test.txt
git commit -m "Add line B"

git checkout exercise-1
git merge main
# Resolve conflict
```

### Exercise 2: Multiple File Conflicts

```powershell
# Create multiple conflicts
git checkout -b exercise-2
echo "Config A" > config1.txt
echo "Config A" > config2.txt
git add .
git commit -m "Add configs A"

git checkout main
echo "Config B" > config1.txt
echo "Config B" > config2.txt
git add .
git commit -m "Add configs B"

git checkout exercise-2
git merge main
# Resolve all conflicts
```

---

## 📚 Additional Resources

- [Git Merge Conflicts Documentation](https://git-scm.com/docs/git-merge)
- [Atlassian Git Merge Tutorial](https://www.atlassian.com/git/tutorials/using-branches/merge-conflicts)
- [GitHub Conflict Resolution Guide](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/addressing-merge-conflicts)
- [VS Code Git Integration](https://code.visualstudio.com/docs/sourcecontrol/overview)

---

## 🎯 Quick Command Reference

```powershell
# Check for conflicts
git status
git diff

# Resolve conflicts
git add <file>
git commit

# Abort merge
git merge --abort

# Use merge tool
git mergetool

# View conflict history
git log --merge

# Show conflicted files
git diff --name-only --diff-filter=U

# Prefer one side
git checkout --ours <file>     # Keep current branch
git checkout --theirs <file>   # Keep incoming branch
```

---

**Last Updated**: February 17, 2026  
**Author**: GitHub Copilot  
**Project**: Student Management System  
**Repository**: https://github.com/IftiaqHossen003/student-management-system

---

## ⚡ TL;DR (Quick Summary)

1. **Create Conflict**: Modify same lines in different branches
2. **Identify**: Look for `<<<<<<`, `=======`, `>>>>>>>` markers
3. **Resolve**: Choose one change, combine both, or rewrite
4. **Test**: Run `mvn clean test`
5. **Complete**: `git add` → `git commit` → `git push`
6. **Best Practice**: Merge frequently, communicate, test thoroughly
