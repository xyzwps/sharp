import classNames from 'classnames';
import { Link, useLocation, useNavigation } from 'react-router-dom';
import styles from './NavLink.module.scss';

type OnCall = () => void;

type NavButtonProps = {
  to: string | OnCall;
  children: React.ReactNode;
};

export default function NavLink({ to, children }: NavButtonProps) {
  const location = useLocation();

  const classes = classNames(styles.sx_nav_link, location.pathname == to && styles.sx_nav_link_active);

  if (typeof to == 'string') {
    return (
      <Link className={classes} to={to}>
        {children}
      </Link>
    );
  } else {
    return (
      <a
        className={classes}
        onClick={(e) => {
          e.preventDefault();
          to();
        }}
      >
        {children}
      </a>
    );
  }
}
