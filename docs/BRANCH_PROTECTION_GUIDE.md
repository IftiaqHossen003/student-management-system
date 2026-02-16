# Branch Protection and PR Workflow Guide

## 📋 Overview
This document provides step-by-step instructions for configuring branch protection rules, creating pull requests, and enforcing quality gates in the Student Management System project.

---

## 🔒 Part 1: Configuring Branch Protection Rules on GitHub

### Step 1: Navigate to Repository Settings
1. Go to your GitHub repository: `https://github.com/IftiaqHossen003/student-management-system`
2. Click on **Settings** tab (⚙️ icon)
3. In the left sidebar, scroll down to **Code and automation** section
4. Click on **Branches**

### Step 2: Add Branch Protection Rule
1. Click **Add rule** or **Add branch protection rule**
2. In the **Branch name pattern** field, enter: `main`
   - This will apply the rule to the `main` branch
   - You can use wildcards like `release/*` for multiple branches

### Step 3: Configure Protection Settings

#### ✅ Required Settings for Production-Grade Protection

##### 1. **Require a pull request before merging**
- ☑️ Check this box
- This prevents direct pushes to main
- All changes must go through PR review process

##### 2. **Require approvals**
- ☑️ Enable "Require approvals"
- Set **Required number of approvals before merging**: `1`
- ☑️ Enable "Dismiss stale pull request approvals when new commits are pushed"
  - This ensures reviewers re-approve after new changes
- ☑️ Enable "Require review from Code Owners" (if CODEOWNERS file exists)

##### 3. **Require status checks to pass before merging**
- ☑️ Check this box
- ☑️ Enable "Require branches to be up to date before merging"
  - This prevents merging outdated branches
- **Search for status checks**: After your first CI run, you'll see:
  - `Run Tests` (from GitHub Actions)
  - `Code Quality Checks`
  - Select these to make them required

##### 4. **Require conversation resolution before merging**
- ☑️ Enable this
- All PR comments must be resolved before merge

##### 5. **Require signed commits** (Optional but recommended)
- ☑️ Enable for additional security
- Ensures commits are cryptographically verified

##### 6. **Require linear history**
- ☑️ Enable to prevent merge commits
- Forces rebase or squash merge strategies

##### 7. **Restrict who can push to matching branches**
- ☑️ Enable this
- Add administrators or specific users who can bypass
- **Important**: Most developers will NOT be in this list

##### 8. **Do not allow bypassing the above settings**
- ☑️ Enable this
- Even admins must follow the rules

##### 9. **Require deployments to succeed before merging** (Optional)
- Enable if you have deployment environments

### Step 4: Additional Settings

#### Lock Branch (Optional)
- Makes the branch read-only
- Prevents any changes including from administrators

#### Allow Force Pushes
- ❌ Keep this DISABLED
- Force pushes can rewrite history and cause data loss

#### Allow Deletions
- ❌ Keep this DISABLED
- Prevents accidental deletion of main branch

### Step 5: Save Changes
- Scroll down and click **Create** or **Save changes**
- Your branch protection is now active! 🎉

---

## 📊 Summary of Protection Rules

| Setting | Status | Purpose |
|---------|--------|---------|
| Require PR before merge | ✅ Required | Prevents direct pushes |
| Require 1 approval | ✅ Required | Peer review mandate |
| Dismiss stale approvals | ✅ Required | Fresh review on changes |
| Require status checks | ✅ Required | CI must pass |
| Branches up to date | ✅ Required | Prevent merge conflicts |
| Resolve conversations | ✅ Required | Address all feedback |
| Block force pushes | ✅ Required | Protect history |
| Restrict push access | ✅ Required | Limited bypass access |

---

## 🔄 Part 2: Creating a Pull Request

### Method 1: Via GitHub Web Interface

#### Step 1: Push Your Branch (if not already pushed)
```powershell
# Ensure you're on the testing branch
git checkout testing/unit-integration-tests

# Push to remote
git push -u origin testing/unit-integration-tests
```

#### Step 2: Create PR on GitHub
1. Navigate to: `https://github.com/IftiaqHossen003/student-management-system`
2. You'll see a yellow banner: **"testing/unit-integration-tests had recent pushes"**
3. Click **Compare & pull request**

#### Step 3: Fill PR Details
```markdown
Title: test: add comprehensive unit and integration tests

Description:
## 🎯 Objective
Implement enterprise-level testing strategy for Student Management System

## ✅ What's Included
- **Unit Tests**: StudentService with Mockito (16 tests)
- **Integration Tests**: StudentController with MockMvc (20+ tests)
- **Repository Tests**: StudentRepository with @DataJpaTest (25+ tests)
- **Entity Tests**: Student entity validation (30+ tests)
- **CI/CD Pipeline**: GitHub Actions with multi-job workflow

## 🧪 Test Coverage
- Service Layer: 100% method coverage
- Controller Layer: Full endpoint + security testing
- Repository Layer: Complete CRUD operations
- Entity Layer: All getter/setter/equals/hashCode

## 📋 Testing Configuration
- H2 in-memory database for tests
- application-test.yml with proper test configuration
- Maven dependencies: H2, Mockito, JUnit 5

## 🚀 CI/CD Features
- Runs on every PR and push to main
- Prevents merge if tests fail
- Uploads test results and coverage reports
- Multi-job pipeline with quality gates

## ✨ Testing Standards
- Follows AAA pattern (Arrange-Act-Assert)
- Covers success, failure, and edge cases
- Tests security authorization (ROLE_USER, ROLE_ADMIN)
- Tests exception handling and validation

## 📖 How to Test Locally
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=StudentServiceTest

# Run with coverage
mvn clean test jacoco:report
```

## 🔍 Changed Files
- Added: StudentServiceTest.java
- Added: StudentControllerIntegrationTest.java
- Added: StudentRepositoryTest.java
- Added: StudentTest.java
- Added: application-test.yml
- Added: .github/workflows/test.yml
- Modified: pom.xml (added H2, Mockito dependencies)

## ✅ Checklist
- [x] All tests pass locally
- [x] Code follows project conventions
- [x] Documentation updated
- [x] No breaking changes
- [x] CI/CD pipeline configured

```

#### Step 4: Set PR Options
- **Base branch**: `main`
- **Compare branch**: `testing/unit-integration-tests`
- **Reviewers**: Add repository owner or team members
- **Labels**: Add `testing`, `enhancement`, `ci-cd`
- **Projects**: (Optional) Add to project board
- **Milestone**: (Optional) Link to milestone

#### Step 5: Create the PR
- Click **Create pull request**
- ✅ Wait for CI checks to complete
- 🔴 If checks fail, PR cannot be merged (as per branch protection)
- ✅ If checks pass, request review

### Method 2: Via GitHub CLI (gh)
```powershell
# Install GitHub CLI (if not installed)
# Download from: https://cli.github.com/

# Authenticate
gh auth login

# Create PR
gh pr create --base main --head testing/unit-integration-tests --title "test: add comprehensive unit and integration tests" --body "See PR description above"
```

---

## 👥 Part 3: Review and Approval Process

### For Reviewers

#### Step 1: Review Code Changes
1. Go to the PR: `https://github.com/IftiaqHossen003/student-management-system/pulls`
2. Click on the PR title
3. Go to **Files changed** tab
4. Review each file:
   - Check code quality
   - Verify test coverage
   - Look for potential issues
   - Ensure best practices

#### Step 2: Add Comments
- Click on any line number to add inline comments
- Ask questions or suggest improvements
- Start a review: Click **Start a review**

#### Step 3: Submit Review
1. Click **Review changes** (top right)
2. Choose one:
   - ✅ **Approve**: Code looks good
   - 💬 **Comment**: Just comments, no approval
   - ❌ **Request changes**: Changes needed before merge
3. Add summary and click **Submit review**

### For PR Author (Responding to Feedback)

#### Step 1: Address Comments
1. Make requested changes in local branch:
```powershell
git checkout testing/unit-integration-tests

# Make your changes
# Edit files as requested

# Commit changes
git add .
git commit -m "test: address review feedback - improve test coverage"

# Push changes
git push origin testing/unit-integration-tests
```

#### Step 2: Resolve Conversations
- On GitHub PR page, click **Resolve conversation** for each addressed comment
- Stale approvals will be dismissed (if configured)
- Reviewers will be notified to re-review

---

## ✅ Part 4: Merging the Pull Request

### Prerequisites Before Merge
1. ✅ All CI checks must pass (green checkmarks)
2. ✅ Required approvals received (minimum 1)
3. ✅ All conversations resolved
4. ✅ Branch is up to date with main
5. ✅ No merge conflicts

### Merge Strategies

#### Option 1: Merge Commit (Default)
```markdown
Merges all commits from feature branch into main
Creates a merge commit
Preserves full history
```
- Use when: You want complete history

#### Option 2: Squash and Merge (Recommended)
```markdown
Combines all commits into single commit
Cleaner main branch history
Easier to revert
```
- Use when: Multiple small commits in PR
- **Best for this project**: Clean commit history

#### Option 3: Rebase and Merge
```markdown
Replays commits one by one on top of main
Linear history without merge commit
```
- Use when: You want linear history

### How to Merge

1. Click **Merge pull request** button (or **Squash and merge**)
2. Confirm merge commit message
3. Click **Confirm merge** or **Confirm squash and merge**
4. ✅ PR is merged!
5. Delete branch: Click **Delete branch** (safe to do after merge)

---

## 🚫 Part 5: What Happens When Rules Are Not Met

### Scenario 1: Tests Fail
```
❌ Some checks were not successful
   ❌ Run Tests — Failed

This pull request cannot be merged due to failing status checks.
```
**Solution**: Fix the failing tests and push again

### Scenario 2: No Approval
```
⚠️ Review required
At least 1 approving review is required by reviewers with write access.

Merging is blocked
```
**Solution**: Request review from team member

### Scenario 3: Branch Outdated
```
⚠️ This branch is out-of-date with the base branch
Merge the latest changes from main into this branch.

Update branch
```
**Solution**: Click **Update branch** or:
```powershell
git checkout testing/unit-integration-tests
git merge main
# Or: git rebase main
git push origin testing/unit-integration-tests
```

### Scenario 4: Unresolved Conversations
```
⚠️ 3 unresolved conversations
Resolve all conversations before merging.
```
**Solution**: Address all review comments

### Scenario 5: Direct Push Attempt
```
remote: error: GH006: Protected branch update failed
remote: error: Required status check "test" is expected.
To https://github.com/IftiaqHossen003/student-management-system.git
 ! [remote rejected] main -> main (protected branch hook declined)
```
**Solution**: Create a PR instead of pushing directly

---

## 🔧 Part 6: Troubleshooting

### Issue: Status Checks Not Appearing

**Problem**: "Require status checks" section is empty

**Solution**:
1. Push a commit that triggers GitHub Actions
2. Wait for the workflow to run at least once
3. Go back to branch protection settings
4. The status check names will now appear in the dropdown
5. Select them as required

### Issue: Unable to Merge Despite Passing Checks

**Problem**: Green checkmarks but merge button disabled

**Solutions**:
1. Check all checkboxes in protection rules are satisfied
2. Ensure you have 1 approval (if required)
3. Update branch if outdated
4. Resolve all conversations
5. Check if you have write access

### Issue: Accidentally Pushed to Main

**Problem**: Committed directly to main before protection was enabled

**Solution**:
```powershell
# Revert the commit
git checkout main
git revert HEAD

# Or reset if not pushed
git reset --hard HEAD~1

# Enable branch protection before continuing
```

---

## 📝 Part 7: Best Practices

### For PR Authors
1. ✅ Keep PRs focused and small (< 500 lines when possible)
2. ✅ Write descriptive PR titles following conventional commits
3. ✅ Add comprehensive PR descriptions
4. ✅ Test locally before pushing
5. ✅ Respond to feedback promptly
6. ✅ Keep branch up to date with main

### For Reviewers
1. ✅ Review within 24 hours
2. ✅ Be constructive and respectful
3. ✅ Focus on code quality and correctness
4. ✅ Suggest improvements, don't demand perfection
5. ✅ Approve when satisfied, request changes when needed

### For Team
1. ✅ Never bypass branch protection
2. ✅ Always run tests locally before pushing
3. ✅ Fix broken builds immediately
4. ✅ Keep main branch always deployable
5. ✅ Document breaking changes

---

## 📚 Additional Resources

- [GitHub Branch Protection Documentation](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches)
- [GitHub Pull Request Best Practices](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests)
- [Conventional Commits Specification](https://www.conventionalcommits.org/)
- [Code Review Best Practices](https://google.github.io/eng-practices/review/)

---

## 🎯 Quick Reference Card

```
BEFORE MERGE CHECKLIST:
☐ All CI checks passed
☐ Minimum 1 approval received
☐ All conversations resolved
☐ Branch up to date with main
☐ No merge conflicts
☐ Tests pass locally

PROTECTION RULES CONFIGURED:
☐ Require PR before merge
☐ Require 1 approval
☐ Dismiss stale approvals
☐ Require status checks (CI)
☐ Require branch up to date
☐ Resolve conversations
☐ Block force pushes
☐ Restrict push access
```

---

**Last Updated**: February 17, 2026  
**Author**: GitHub Copilot  
**Project**: Student Management System  
**Repository**: https://github.com/IftiaqHossen003/student-management-system
