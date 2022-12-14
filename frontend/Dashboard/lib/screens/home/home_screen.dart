import 'package:smart_admin_dashboard/responsive.dart';
import 'package:smart_admin_dashboard/screens/dashboard/dashboard_screen.dart';
import 'package:flutter/material.dart';
import '../../core/constants/color_constants.dart';
import '../Pages/ChooseParkingSlotScreen.dart';
import '../Pages/ReservationWithSearch.dart';
import '../dashboard/components/RecentUser2.dart';
import '../dashboard/components/calendart_widget.dart';
import '../login/login_screen.dart';
import 'components/side_menu.dart';

class HomeScreen extends StatefulWidget {
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {

  int controller = 0;
  List list = [DashboardScreen(),RecentUsers2(),ListReservations0(), ChooseParkingSlotScreen(),CalendarWidget()];
  @override
  Widget build(BuildContext context) {


    return Scaffold(
   drawer: Drawer(
     child: SingleChildScrollView(
       // it enables scrolling
       child: Column(
         children: [
           DrawerHeader(

               child: Column(
                 mainAxisAlignment: MainAxisAlignment.center,
                 children: [
                   SizedBox(
                     height: defaultPadding * 3,
                   ),
                   Image.asset(
                     "assets/logo/logo_icon.png",
                     scale: 5,
                   ),
                   SizedBox(
                     height: defaultPadding,
                   ),
                   Text("Smart Parking - Dashboard")
                 ],
               )),
           DrawerListTile(
             selected_color: controller ==0 ? Color(0xff34d186) : Colors.black  ,
             text_color: controller ==0 ? Colors.white : Colors.white,
             title: "Dashboard",
             svgSrc: "assets/icons/menu_tran.svg",
             press: () {
               setState(() { controller=0; });
             },
           ),
           DrawerListTile(
             selected_color: controller ==1 ? Color(0xff34d186) : Colors.black  ,
             text_color: controller ==1 ? Colors.white : Colors.white,
             title: "Users",
             svgSrc: "assets/icons/menu_tran.svg",
             press: () {
               setState(() { controller=1; });
             },
           ),
           DrawerListTile(
             selected_color: controller ==2 ? Color(0xff34d186) : Colors.black  ,
             text_color: controller ==2 ? Colors.white : Colors.white,
             title: "Reservations",
             svgSrc: "assets/icons/menu_task.svg",
             press: () {
               setState(() { controller=2; });

               // Navigator.pushReplacementNamed(context, Routes.calendar);
             },
           ),
           DrawerListTile(
             selected_color: controller ==3 ? Color(0xff34d186) : Colors.black  ,
             text_color: controller ==3 ? Colors.white : Colors.white,
             title: "Parking Live",
             svgSrc: "assets/icons/menu_task.svg",
             press: () {
               setState(() { controller=3; });

               // Navigator.pushReplacementNamed(context, Routes.calendar);
             },
           ),
           DrawerListTile(
             selected_color: controller ==4 ? Color(0xff34d186)  : Colors.black  ,
             text_color: controller ==4? Colors.white : Colors.white,
             title: "Calendar",
             svgSrc: "assets/icons/menu_doc.svg",
             press: () {
               setState(() { controller=4; });
             },
           ),
           DrawerListTile(
             selected_color: controller ==5 ? Color(0xff34d186)  : Colors.black  ,
             text_color: controller ==5 ? Colors.white : Colors.white,
             title: "Logout",
             svgSrc: "assets/icons/menu_store.svg",
             press: () {},
           ),
         ],
       ),
     ),
   ),
      body: SafeArea(
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // We want this side menu only for large screen
            if (Responsive.isDesktop(context))
              Expanded(
                // default flex = 1
                // and it takes 1/6 part of the screen
                child: Drawer(
                  child: SingleChildScrollView(
                    // it enables scrolling
                    child: Column(
                      children: [
                        SizedBox(
                          height: defaultPadding * 0.5,
                        ),
                        Container(
                          margin: EdgeInsets.only(left: defaultPadding),
                          padding: EdgeInsets.symmetric(
                            horizontal: defaultPadding,
                            vertical: defaultPadding / 2,
                          ),
                          decoration: BoxDecoration(
                            color: secondaryColor,
                            borderRadius: const BorderRadius.all(Radius.circular(10)),
                            border: Border.all(color: Colors.white10),
                          ),
                          child: Row(
                            children: [
                              CircleAvatar(
                                backgroundImage: AssetImage("assets/images/profile_pic.png"),
                              ),
                              if (!Responsive.isMobile(context))
                                Padding(
                                  padding:
                                  const EdgeInsets.symmetric(horizontal: defaultPadding / 2),
                                  child: Text("ADMIN"),
                                ),
                            ],
                          ),
                        ),
                        DrawerHeader(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                SizedBox(
                                  height: defaultPadding * 0.5,
                                ),
                                Image.asset(
                                  "assets/logo/logo_icon.png",
                                  scale: 5,
                                ),
                                SizedBox(
                                  height: defaultPadding,
                                ),
                                Text("Smart Parking - Dashboard")
                              ],
                            )),
                        DrawerListTile(
                          selected_color: controller ==0 ? Color(0xff34d186) : Colors.black  ,
                          text_color: controller ==0 ? Colors.white : Colors.white,
                          title: "Dashboard",
                          svgSrc: "assets/icons/menu_tran.svg",
                          press: () {
                            setState(() { controller=0; });
                          },
                        ),
                        DrawerListTile(
                          selected_color: controller ==1 ? Color(0xff34d186) : Colors.black  ,
                          text_color: controller ==1 ? Colors.white : Colors.white,
                          title: "Users",
                          svgSrc: "assets/icons/menu_tran.svg",
                          press: () {

                            setState(() { controller=1; });
                          },
                        ),
                        DrawerListTile(
                          selected_color: controller ==2 ? Color(0xff34d186) : Colors.black  ,
                          text_color: controller ==2 ? Colors.white : Colors.white,
                          title: "Reservations",
                          svgSrc: "assets/icons/menu_task.svg",
                          press: () {
                            setState(() { controller=2; });
                          },
                        ),
                        DrawerListTile(
                          selected_color: controller ==3 ? Color(0xff34d186) : Colors.black  ,
                          text_color: controller ==3 ? Colors.white : Colors.white,
                          title: "Parking Live",
                          svgSrc: "assets/icons/menu_task.svg",
                          press: () {
                            setState(() { controller=3; });

                            // Navigator.pushReplacementNamed(context, Routes.calendar);
                          },
                        ),
                        DrawerListTile(
                          selected_color: controller ==4 ? Color(0xff34d186) : Colors.black  ,
                          text_color: controller ==4 ? Colors.white : Colors.white,
                          title: "Calendar",
                          svgSrc: "assets/icons/menu_doc.svg",
                          press: () {
                            setState(() { controller=4; });

                            // Navigator.pushReplacementNamed(context, Routes.calendar);
                          },
                        ),
                        DrawerListTile(
                          selected_color: controller ==5 ? Color(0xff34d186)  : Colors.black  ,
                          text_color: controller ==5? Colors.white : Colors.white,
                          title: "Logout",
                          svgSrc: "assets/icons/logout.svg",
                          press: () {
                            showDialog(
                                context: context,
                                builder: (_) {
                                  return AlertDialog(
                                      title: Center(
                                        child: Column(
                                          children: [
                                            Icon(Icons.warning_outlined,
                                                size: 36, color: Colors.red),
                                            SizedBox(height: 20),
                                            Text("Logout"),
                                          ],
                                        ),
                                      ),
                                      content: Container(
                                        color: secondaryColor,
                                        height: 70,
                                        child: Column(
                                          children: [
                                            Text(
                                                "Are you sure want to delete logout?"),
                                            SizedBox(
                                              height: 16,
                                            ),
                                            Row(
                                              mainAxisAlignment: MainAxisAlignment.center,
                                              children: [
                                                ElevatedButton.icon(
                                                    icon: Icon(
                                                      Icons.close,
                                                      size: 14,
                                                    ),
                                                    style: ElevatedButton.styleFrom(
                                                        primary: Colors.grey),
                                                    onPressed: () {
                                                      Navigator.of(context).pop();
                                                    },
                                                    label: Text("Cancel")),
                                                SizedBox(
                                                  width: 20,
                                                ),
                                                ElevatedButton.icon(
                                                    icon: Icon(
                                                      Icons.logout,
                                                      size: 14,
                                                    ),
                                                    style: ElevatedButton.styleFrom(
                                                        primary: Colors.red),
                                                    onPressed: () {
                                                      Navigator.push(
                                                        context,
                                                        MaterialPageRoute(builder: (context) => Login(title: "Wellcome to the Smart Parking Admin & Dashboard Panel")),
                                                      );

                                                    },
                                                    label: Text("Confirm"))
                                              ],
                                            )
                                          ],
                                        ),
                                      ));
                                });

                          },
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            Expanded(
              // It takes 5/6 part of the screen
              flex: 5,
              child: list[controller],
            ),
          ],
        ),
      ),
    );
  }
}
