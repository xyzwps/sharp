import { Link } from 'react-router-dom';
import styles from './NavLink.module.scss';

type OnCall = () => void;

type NavButtonProps = {
  to: string | OnCall;
  children: React.ReactNode;
};

export default function NavLink({ to, children }: NavButtonProps) {
  if (typeof to == 'string') {
    return (
      <Link className={styles.sx_nav_link} to={to}>
        {children}
      </Link>
    );
  } else {
    return (
      <a
        className={styles.sx_nav_link}
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
