import { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

import { Button } from 'react-bootstrap';
import { createPost } from '../../apis/post';
import { useNavigate } from 'react-router-dom';
import MarkdownEditor from '../../components/MarkdownEditor';
import AuthHelper from '../common/AuthHelper';
import { Flex, Loader } from '@mantine/core';
import { showNotification } from '@mantine/notifications';
import { toastError } from '../../error';

export default function CreatePostPage() {
  return (
    <AuthHelper
      onAuth={() => <PostCreator />}
      onUnauth="/a/login"
      onLoading={() => <Loader size="xl" variant="bars" />}
    />
  );
}

function PostCreator() {
  const navigate = useNavigate();
  const [idem] = useState<string>(uuidv4());
  const [content, setContent] = useState<string>('');
  const [title, setTitle] = useState<string>('');

  const handleSave = () => {
    const trimedTitle = title.trim();
    if (trimedTitle.length <= 0) {
      showNotification({ message: '请填写标题，不要留空白哟~', autoClose: 3000 });
      return;
    }

    createPost({ type: 'MD', title, content, idem })
      .then((post) => navigate(`/posts/${post.id}`))
      .catch((err) => toastError(err, '创建文章失败'));
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
      <Flex align="center" justify="space-between" style={{ padding: '8px 16px' }}>
        正在创作新文章 <Button onClick={handleSave}>保存</Button>
      </Flex>
    </div>
  );
}
