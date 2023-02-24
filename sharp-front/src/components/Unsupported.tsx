import styles from './Unsupported.module.scss';

type Props = {
  children?: React.ReactNode | null;
};

export default function Unsupported({ children }: Props) {
  return (
    <div className={styles.sxUnsupported}>
      <div className={styles.sxUnsupportedTitle}>尚未支持的功能</div>
      <div className={styles.sxUnsupportedDesc}>{children}</div>
    </div>
  );
}
