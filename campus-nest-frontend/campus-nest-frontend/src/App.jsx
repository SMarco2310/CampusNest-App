import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Registration from "./pages/Registration";
import HostelDetails from "./pages/HostelDetails";
import AuthLayout from "./layouts/AuthLayOut";
import HostelSearchPage from "./pages/HostelSearchPage";
import HomeLayout from "./layouts/HomeLayout";
import HostelDetailsWrapper from "./components/HostelDetailsWrapper";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/home" element={<Home />}></Route>
        <Route element={<HomeLayout />}>
          <Route path="/hostel/:id" element={<HostelDetailsWrapper />}></Route>
          <Route path="/hostels" element={<HostelSearchPage />}></Route>
        </Route>
        {/* Auth layout elements */}
        <Route element={<AuthLayout />}>
          <Route path="/auth" element={<Registration />}></Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
