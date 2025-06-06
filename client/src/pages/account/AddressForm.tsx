import {
  useCreateAddressMutation,
  useUpdateAddressByIdMutation,
} from "@/features/addresses/addressApi";
import { useGetCountriesQuery } from "@/features/countries/countryApi";
import { Country } from "@/types/Country";
import { UserDetails } from "@/types/User";
import { useEffect, useState } from "react";

type Props = {
  user: UserDetails;
};

const AddressForm = ({ user }: Props) => {
  const [form, setForm] = useState({
    address: "",
    address2: "",
    district: "",
    city: "",
    country: "",
    postalCode: "",
    phone: "",
  });

  const [countries, setCountries] = useState<Country[]>([]);

  const [updateAddress, { isLoading: isAddressUpdating }] =
    useUpdateAddressByIdMutation();
  const [createAddress] = useCreateAddressMutation();
  const { data: countriesList } = useGetCountriesQuery();

  useEffect(() => {
    if (countriesList) {
      setCountries(countriesList);
    }
  }, [countriesList, setCountries]);

  useEffect(() => {
    if (user?.address) {
      setForm({
        address: user?.address?.address || "",
        address2: user?.address?.address2 || "",
        district: user?.address?.district || "",
        city: user?.address?.city?.name || "",
        country: user?.address?.city?.country.name || "",
        postalCode: user?.address?.postalCode || "",
        phone: user?.address?.phone || "",
      });
    }
  }, [setForm, user]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const {name, value} = e.target;
    setForm((prev) => ({
        ...prev,
        [name]: value === "None" ? "" : value,
    }));
  };

  const handleSubmitAddressForm = async (e: React.FormEvent) => {
    e.preventDefault();

    if (
      !form.address.trim() ||
      !form.district.trim() ||
      !form.city.trim() ||
      !form.country.trim() ||
      !form.postalCode.trim() ||
      !form.phone.trim()
    ) {
      alert("Must provide complete address. Address 2 field can be blank.");
      return;
    }

    if (form.address.trim().length < 2 || form.address.trim().length > 50) {
      alert("Please provide valid Address.");
      return;
    }

    if (form.district.trim().length < 2 || form.district.trim().length > 50) {
      alert("Please provide valid District.");
      return;
    }

    if (form.city.trim().length < 2 || form.city.trim().length > 50) {
      alert("Please provide valid City.");
      return;
    }

    if (form.postalCode.trim().length < 6 || form.postalCode.trim().length > 10) {
      alert(
        "Postal Code must have at least 6 digits and max 10 digits. Please provide a valid Postal Code."
      );
      return;
    }

    if (form.phone.trim().length < 10 || form.phone.trim().length > 16) {
      alert(
        "Phone number must be at least 10 digits long and max 16 digits. Please provide a valid phone number."
      );
      return;
    }

    if (user?.address?.id) {
      try {
        await updateAddress({
          id: user?.address.id,
          data: {
            address: form.address,
            address2: form.address2,
            district: form.district,
            city: form.city,
            country: form.country,
            postalCode: form.postalCode,
            phone: form.phone,
          },
        });
        alert("Address successfully updated.");
      } catch (err: unknown) {
        const message = err instanceof Error ? err.message : "Unknown error";
        alert(`Update failed: ${message}`);
      }
    } else {
      try {
        await createAddress({
          address: form.address,
          address2: form.address2,
          district: form.district,
          city: form.city,
          country: form.country,
          postalCode: form.postalCode,
          phone: form.phone,
          userId: user!.id,
        });
        alert("Address successfully created.");
      } catch (err: unknown) {
        const message = err instanceof Error ? err.message : "Unknown error";
        alert(`Update failed: ${message}`);
      }
    }
  };

  return (
    <div className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 rounded-lg p-4">
      <h2 className="text-xl font-semibold text-gold-500 py-4">
        Address Details
      </h2>
      <form onSubmit={handleSubmitAddressForm} className="flex flex-col">
        <div className="my-1 flex flex-row justify-between items-center">
          <label htmlFor="address">Address</label>
          <input
            type="text"
            id="address"
            name="address"
            value={form.address}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>
        <div className="my-1 flex flex-row justify-between items-center">
          <label htmlFor="address2">Address 2</label>
          <input
            type="text"
            id="address2"
            name="address2"
            value={form.address2}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>
        <div className="my-1 flex flex-row justify-between items-center">
          <label htmlFor="district">District</label>
          <input
            type="text"
            id="district"
            name="district"
            value={form.district}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>
        <div className="my-1 flex flex-row justify-between items-center">
          <label htmlFor="city">City</label>
          <input
            type="text"
            id="city"
            name="city"
            value={form.city}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>
        <div className="my-1 flex flex-row justify-between items-center">
          <label htmlFor="country">Country</label>
          <select
            name="country"
            id="country"
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4 w-44"
          >
            <option value={user?.address?.city?.country?.name}>
              {user?.address?.city?.country?.name}
            </option>
            {countries
              .filter(
                (country) => country.name !== user?.address?.city?.country?.name
              )
              .map((c) => (
                <option key={c.id} value={c.name}>
                  {c.name}
                </option>
              ))}
          </select>
        </div>
        <div className="my-1 flex flex-row justify-between items-center">
          <label htmlFor="postalCode">Postal Code</label>
          <input
            type="text"
            id="postalCode"
            name="postalCode"
            value={form.postalCode}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>
        <div className="my-1 flex flex-row justify-between items-center">
          <label htmlFor="phone">Phone</label>
          <input
            type="text"
            id="phone"
            name="phone"
            value={form.phone}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>
        <button
          type="submit"
          disabled={isAddressUpdating}
          className="bg-red-500 dark:bg-charcoal-800 text-charcoal-800 dark:text-red-500 px-2 py-1 mt-4 rounded-sm mx-auto"
        >
          {isAddressUpdating ? "Updating" : "Save changes"}
        </button>
      </form>
    </div>
  );
};

export default AddressForm;
