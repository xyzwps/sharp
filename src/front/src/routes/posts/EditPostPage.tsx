import { Button } from '@mantine/core';
import { useState } from 'react';
import { Navigate, useLoaderData, useNavigate } from 'react-router-dom';

import { RichPost, updatePost } from '../../apis/post';
import MarkdownEditor from '../../components/MarkdownEditor';
import Unsupported from '../../components/Unsupported';
import { toastError } from '../../error';
import AuthHelper from '../common/AuthHelper';

export default function EditPostPage() {
  const post = useLoaderData() as RichPost;

  return (
    <AuthHelper
      onAuth={(user) => {
        if (user.id != post.author?.id) {
          return <Navigate to={`/posts/${post.id}`} />;
        } else {
          switch (post.content.type) {
            case 'MD':
              return <MarkdownPostEditor post={post} />;
            default:
              return <Unsupported>尚未支持的文章格式: {post.content.type}</Unsupported>;
          }
        }
      }}
      onUnauth={`/posts/${post.id}`}
    />
  );
}

function MarkdownPostEditor({ post }: { post: RichPost }) {
  const navigate = useNavigate();
  const [content, setContent] = useState<string>(post.content.content);
  const [title, setTitle] = useState<string>(post.title);

  const handleSave = () => {
    updatePost(post.id, { title, content })
      .then((r) => navigate(`/posts/${r.postId}`))
      .catch((err) => toastError(err, '更新文章失败'));
  };

  return (
    <div>
      <input
        style={{ padding: '8px 16px', fontSize: '2rem', fontWeight: 'bold', width: '100%' }}
        value={title}
        placeholder="请在此输入标题"
        onChange={(e) => setTitle(e.target.value)}
      />
      <MarkdownEditor value={content} onChange={setContent} style={{ height: 'calc(100vh - 124px)' }} />
      <div style={{ textAlign: 'right', padding: '8px 16px' }}>
        <Button onClick={handleSave}>保存</Button>
      </div>
    </div>
  );
}
