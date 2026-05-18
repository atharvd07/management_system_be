Step 1: made changes in UserRepository.java
Step 2: From Git Staging tab need to click on file the add to index.
step 3: add message and click on "Commit and Push"
step 4: enter atharvd07 in username and PAT in password

How to create PAT

Go to git website
setting- developer settings- Personal access tokens(classic)- Generate new token(classic)- tick on "repo" to give full repo access

Branches

both Local and Remote branch should be same "main"

Remote Branch: Below repo name you can see branches like Main, Master... Select main from that
Local Branch: When you click on "Push Head" then under branch field mention "main" if master or anything else is there.

Notes:
First commit and then push head so you can see sourch and destination branches
Main tabs on STS: Git Repository, Git Staging

CREATE TABLE feedback (
    feedback_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    feedback_dept VARCHAR(50) NOT NULL,
    feedback_details VARCHAR(1000) NOT NULL
);

select * from feedback
select f.feedback_id, f.user_id, u.name, f.feedback_dept, f.feedback_details from feedback f join users u on f.user_id= u.id

adminprofile@gmail.com 1234567890
usera@gmail.com 11
userb@gmail.com 22