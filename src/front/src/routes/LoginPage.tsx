import { TextInput, Checkbox, Button, Group, Box, Card, PasswordInput, Text, Space } from '@mantine/core';
import { useForm } from '@mantine/form';
import { Navigate, redirect } from 'react-router-dom';
import AuthHelper from './common/AuthHelper';
import { useAuthStore } from '../store/auth';
import { rules, s } from './common/form-validator';
import { toastError } from '../error';

export default function LoginPage() {
  return <AuthHelper onAuth={() => <Navigate to="/profile" />} onUnauth={() => <LoginForm />} />;
}

function LoginForm() {
  const form = useForm({
    initialValues: {
      username: '',
      password: '',
      terms: false,
    },

    validate: {
      username: rules(
        s.minLength(2, '用户名最少应包含2个字符'),
        s.maxLength(20, '用户名最多可包含20个字符'),
        s.pattern(/^[a-zA-Z0-9_-]+$/, '用户名只能包含大小写字母、数组、下划线(_)和短横线(-)')
      ),
      password: rules(
        s.minLength(5, '密码最少应该包含5个字符'),
        s.maxLength(32, '密码最多可包含32个字符'),
        s.pattern(
          /^[a-zA-Z0-9`~!@#$%^&*()_\-+=]+$/,
          '密码只能包含大小写字母、数字和键盘数字行上的特殊字符（如“-”、“@”等）'
        )
      ),
    },
  });

  const login = useAuthStore((state) => state.login);

  const handleSumbit = form.onSubmit((values) => {
    login({ username: values.username, password: values.password })
      .then(() => redirect('/profile'))
      .catch((err) => toastError(err, '登录失败'));
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
            登录
          </Text>
        </Card.Section>
        <form onSubmit={handleSumbit}>
          <TextInput withAsterisk label="用户名" placeholder="在此填写用户名" {...form.getInputProps('username')} />
          <Space h="sm" />
          <PasswordInput withAsterisk label="密码" placeholder="在此填写密码" {...form.getInputProps('password')} />
          <Space h="sm" />
          <Checkbox mt="md" label="同意《用户协议》" {...form.getInputProps('terms', { type: 'checkbox' })} />
          <Group position="right" mt="md">
            <Button disabled={!form.values.terms} type="submit">
              登录
            </Button>
          </Group>
        </form>
      </Card>
    </Box>
  );
}
