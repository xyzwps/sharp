import { NavLinkProps, NavLink, NavbarBrandProps, NavbarBrand } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

type Props = Omit<NavbarBrandProps, 'href' | 'onClick'> & { to?: string };

export default function RouterNavBarBrand({ to, ...rest }: Props) {
  const navigate = useNavigate();

  return (
    <NavbarBrand
      {...rest}
      href={to}
      onClick={(e) => {
        e.preventDefault();
        if (to) navigate(to);
      }}
    />
  );
}
