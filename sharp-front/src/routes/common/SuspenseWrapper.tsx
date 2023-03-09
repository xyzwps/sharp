import { Loader } from '@mantine/core';
import React, { ComponentType, Suspense } from 'react';

interface SuspenseWrapperProps {
  factory: () => Promise<{ default: ComponentType<unknown> }>;
}

/**
 * @see https://dev.to/omogbai/code-splitting-with-react-router-v6-react-lazy-and-suspense-in-simple-terms-5365
 */
const SuspenseWrapper = (props: SuspenseWrapperProps) => {
  const LazyComponent = React.lazy(props.factory);
  return (
    <Suspense
      fallback={
        <div className="loader-container">
          <Loader variant="bars" />
        </div>
      }
    >
      <LazyComponent />
    </Suspense>
  );
};

export default SuspenseWrapper;
