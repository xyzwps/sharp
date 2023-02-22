package run.antleg.sharp.config.hibernate;

import run.antleg.sharp.modules.todo.model.TodoId;

public class TodoIdUserType extends StringRecordUserType<TodoId> {
    public TodoIdUserType() {
        super(TodoId.class, TodoId::new, TodoId::value);
    }
}