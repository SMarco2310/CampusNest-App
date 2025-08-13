import { Outlet } from "react-router-dom";
import NavBar from "../components/NavBar";
function HomeLayout() {
  return (
    <>
      <NavBar />
      <Outlet />
    </>
  );
}
export default HomeLayout;
