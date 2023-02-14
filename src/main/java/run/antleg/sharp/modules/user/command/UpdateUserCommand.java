package run.antleg.sharp.modules.user.command;

import run.antleg.sharp.modules.user.model.User;

public class UpdateUserCommand extends CreateUserCommand {

    public void updateUser(User user) {
        user.setDisplayName(this.getDisplayName());
    }
}
