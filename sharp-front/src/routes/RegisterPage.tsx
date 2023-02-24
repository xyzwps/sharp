import { Box, Card, Text, TextInput, PasswordInput, Space, Checkbox, Group, Button } from '@mantine/core';
import { useForm } from '@mantine/form';
import { Navigate, useNavigate } from 'react-router';
import AuthHelper from './common/AuthHelper';
import { rules, s } from './common/form-validator';
import { register } from '../apis/register';
import { showNotification } from '@mantine/notifications';
import { toastError } from '../error';

export default function RegisterPage() {
  return <AuthHelper onAuth={() => <Navigate to="/profile" />} onUnauth={() => <RegisterForm />} />;
}

function RegisterForm() {
  const navigate = useNavigate();
  const form = useForm({
    initialValues: {
      username: '',
      password: '',
      password2: '',
      terms: false,
    },
    validate: {
      username: rules(
        s.minLength(2, '用户名最少应包含2个字符'),
        s.maxLength(20, '用户名最多可包含20个字符'),
        s.pattern(/^[a-zA-Z0-9_-]+$/, '用户名只能包含大小写字母、数组、下划线(_)和短横线(-)')
      ),
      password: rules(
        s.minLength(8, '密码最少应该包含8个字符'),
        s.maxLength(32, '密码最多可包含32个字符'), // TODO: 密码强度
        s.pattern(
          /^[a-zA-Z0-9`~!@#$%^&*()_\-+=]+$/,
          '密码只能包含大小写字母、数字和键盘数字行上的特殊字符（如“-”、“@”等）'
        )
      ),
      password2: (v, values) => (v !== values.password ? '两次输入密码不同' : null),
    },
  });

  const handleSumbit = form.onSubmit((values) => {
    register({ username: values.username, password: values.password })
      .then(() => {
        showNotification({ message: '注册成功！快去登录吧~', autoClose: 3000 });
        navigate('/a/login');
      })
      .catch((err) => toastError(err, '注册失败'));
  });

  return (
    <Box sx={{ maxWidth: 320 }} mx="auto">
      <Card withBorder>
        <Card.Section inheritPadding>
          <Text
            variant="gradient"
            style={{ padding: 12 }}
            gradient={{ from: 'indigo', to: 'cyan', deg: 45 }}
            ta="center"
            fz="xl"
            fw={700}
          >
            注册
          </Text>
        </Card.Section>
        <form onSubmit={handleSumbit}>
          <TextInput
            withAsterisk
            autoComplete="off"
            label="用户名"
            placeholder="在此填写用户名"
            {...form.getInputProps('username')}
          />
          <Space h="sm" />
          <PasswordInput
            withAsterisk
            autoComplete="new-password"
            label="密码"
            placeholder="在此填写密码"
            {...form.getInputProps('password')}
          />
          <Space h="sm" />
          <PasswordInput
            withAsterisk
            autoComplete="new-password"
            label="确认密码"
            placeholder="再次输入密码以避免出错"
            {...form.getInputProps('password2')}
          />
          <Space h="sm" />
          <Checkbox mt="md" label="同意《用户协议》" {...form.getInputProps('terms', { type: 'checkbox' })} />
          <Group position="right" mt="md">
            <Button disabled={!form.values.terms} type="submit">
              注册
            </Button>
          </Group>
        </form>
      </Card>
    </Box>
  );
}
