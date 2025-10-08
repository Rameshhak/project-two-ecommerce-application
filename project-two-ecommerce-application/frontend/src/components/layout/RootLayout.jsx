import React from 'react'
import { Outlet } from 'react-router'
import Header from '../layout/Header.jsx'

function RootLayout() {
  return (
    <div>
      <Header />
      <Outlet />
    </div>
  )
}

export default RootLayout
