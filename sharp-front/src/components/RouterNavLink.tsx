import { NavLinkProps, NavLink } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

type Props = Omit<NavLinkProps, 'href' | 'onClick'> & { to?: string };

export default function RouterNavLink({ to, ...rest }: Props) {
  const navigate = useNavigate();

  return (
    <NavLink
      {...rest}
      href={to}
      onClick={(e) => {
        e.preventDefault();
        if (to) navigate(to);
      }}
    />
  );
}
