import { useEffect, useState } from 'react';

type Props = {
  seconds: number;
  stage1: (seconds: number) => React.ReactNode;
  stage2: () => React.ReactNode;
};

export default function Delay({ seconds, stage1, stage2 }: Props) {
  const [left, setLeft] = useState<number>(Math.ceil(Math.max(1, seconds)));

  useEffect(() => {
    const t = setTimeout(() => {
      setLeft(Math.round(left - 1));
      clearTimeout(t);
    }, 1000);
  }, [left]);

  return <>{left > 0 ? stage1(left) : stage2()}</>;
}
