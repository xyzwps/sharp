package run.antleg.sharp.config.hibernate;


import run.antleg.sharp.modules.post.model.PostId;

public class PostIdUserType  extends LongRecordUserType<PostId>{
    public PostIdUserType() {
        super(PostId.class, PostId::new, PostId::value);
    }
}