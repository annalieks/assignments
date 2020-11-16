using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Solid1
{
    //Який принцип S.O.L.I.D. порушено? Виправте!
    // Single responsibility principle
    class Item
    {

    }
    
    class Order
    {
        private List<Item> itemList;

        internal List<Item> ItemList
        {
            get
            {
                return itemList;
            }

            set
            {
                itemList = value;
            }
        }
        
        public void AddItem(Item item) {/*...*/}
        public void DeleteItem(Item item) {/*...*/}
        
        public void CalculateTotalSum() {/*...*/}
        public void GetItems() {/*...*/}
        public void GetItemCount() {/*...*/}
    }
    
    class Program
    {
        static void Main()
        {
        }
    }
}
