import { useParams } from "react-router-dom";
import HostelDetails from "../pages/HostelDetails"; // adjust path as needed

function HostelDetailsWrapper() {
  const { id } = useParams();
  return <HostelDetails hostelId={id} />;
}
export default HostelDetailsWrapper;
