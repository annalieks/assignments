import DatabasesView from './views/DatabasesView/DatabasesView';
import DatabaseView from './views/DatabaseView/DatabaseView';
import TableView from './views/TableView/TableView';
import { ToastContainer } from 'react-toastify';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import 'semantic-ui-css/semantic.min.css';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';
import IntersectView from './views/IntersectView/IntersectView';

const App = () => {
  return (
    <>
      <ToastContainer
        autoClose={2000}
        theme="dark" />
      <Router>
        <Routes>
          <Route index element={<DatabasesView />} />
          <Route exact path="/database/:id" element={<DatabaseView />} />
          <Route exact path="/:dbId/table/:id" element={<TableView />} />
          <Route exact path="/:id/intersect" element={<IntersectView />} />
          {/* <PrivateRoute exact path="/home" component={HomePage}/>
        <Route exact path="/login" component={() => <AuthForm register={false}/>}/>
        <Route exact path="/signup" component={() => <AuthForm register={true}/>}/>
        <PrivateRoute exact path="/course/:courseId" component={CoursePage}/>
        <PrivateRoute exact path="/user/:id" component={UserPage}/>
        <PrivateRoute exact path="/create/course" component={CreateCoursePage}/>
        <PrivateRoute exact path="/todo" component={ToDo} /> */}
        </Routes>
      </Router>
    </>
  );
}

export default App;
